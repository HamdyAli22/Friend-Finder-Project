import { Component, OnInit } from '@angular/core';
import {Friendship} from '../../../../model/friendship';
import {FriendshipService} from '../../../../service/friendship.service';
import {MessageHandlerService} from '../../../../service/message-handler.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../../../service/auth.service';

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent implements OnInit {

  friends: Friendship[] = [];
  currentUserId = Number(localStorage.getItem('userId') || 0);
  userId!: number;

  showBar = true;
  isOwner = false;


  serverBase = 'http://localhost:8081';
  currentProfileImage?: string;
  currentCoverImage?: string;

  constructor(private friendshipService: FriendshipService,
              private messageService: MessageHandlerService,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService) { }

  ngOnInit(): void {

    this.authService.getProfile().subscribe();

    this.authService.profileImage$.subscribe(img => {
      this.currentProfileImage = img
        ? this.serverBase + img
        : 'assets/images/users/default-avatar-icon.jpg';
    });

    this.authService.coverImage$.subscribe(img => {
      this.currentCoverImage = img
        ? this.serverBase + img
        : 'assets/images/covers/default-cover.jpg';
    });

    if (this.router.url.includes('/timeline')) {
      this.showBar = false;
    }

    this.route.queryParams.subscribe(params => {
      this.userId = params['userId'] ? +params['userId'] : this.currentUserId;
      this.isOwner = this.userId === this.currentUserId;
      this.loadFriends();
    });
  }

  loadFriends(): void {
    this.friendshipService.getUserFriends(this.userId).subscribe({
      next: (data) => {
        this.friends = data;
        this.checkFriendStatus();
      },
      error: (err) => {
        this.messageService.handleError(err);
      }
    });
  }

  checkFriendStatus(): void {

    // جلب أصدقاء المستخدم الحالي (My Friends) وطلبات الصداقة المرسلة
    this.friendshipService.getUserFriends(this.currentUserId).subscribe(myFriends => {

      this.friendshipService.getPendingSentRequests().subscribe(sentRequests => {

        this.friends = this.friends.map(friend => {

          // لو صاحب الحساب نفسه
          if (this.isOwner) {
            const existingFriend = myFriends.find(f => f.id === friend.id);
            if (existingFriend) {
              return { ...friend, isFriend: true, requestSent: false, friendId: existingFriend.friendId };
            }

            const pending = sentRequests.find(r => r.id === friend.id);
            if (pending) {
              return { ...friend, isFriend: false, requestSent: true, friendId: pending.friendId };
            }

            return { ...friend, isFriend: false, requestSent: false, friendId: undefined };
          } else {
            // لو بتتفرج على أصدقاء حد تاني
            const existingFriend = myFriends.find(f => f.id === friend.id);
            if (existingFriend) {
              return { ...friend, isFriend: true, requestSent: false, friendId: existingFriend.friendId };
            }

            const pending = sentRequests.find(r => r.id === friend.id);
            if (pending) {
              return { ...friend, isFriend: false, requestSent: true, friendId: pending.friendId };
            }

            return { ...friend, isFriend: false, requestSent: false, friendId: undefined};
          }

        });

      });

    });
  }



  unfriend(friend: Friendship): void {
    this.friendshipService.cancelFriendRequest(friend.friendId).subscribe({
      next: () => {
        friend.isFriend = false;
        friend.requestSent = false;
        friend.friendId = undefined;
      },
      error: (err) => {
        this.messageService.handleError(err);
      }
    });
  }


  addFriend(friend: Friendship): void {
    this.friendshipService.sendFriendRequest(friend.id).subscribe({
      next: (response) => {
        friend.requestSent = true;
        friend.isFriend = false;
        friend.friendId = response.id;
      },
      error: (err) => {
        this.messageService.handleError(err);
      }
    });
  }

  cancelRequest(friend: Friendship): void {
    console.log('friendId:', friend.friendId);
    if (!friend.friendId) return;

    this.friendshipService.cancelFriendRequest(friend.friendId).subscribe({
      next: () => {
        friend.requestSent = false;
        friend.isFriend = false;
        friend.friendId = undefined;
      },
      error: (err) => {
        this.messageService.handleError(err);
      }
    });
  }


}
