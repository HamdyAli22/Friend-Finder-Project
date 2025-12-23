import { Component, OnInit } from '@angular/core';
import {Friendship} from '../../../../model/friendship';
import {FriendshipService} from '../../../../service/friendship.service';
import {MessageHandlerService} from '../../../../service/message-handler.service';
import {ActivatedRoute, Router} from '@angular/router';

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

  constructor(private friendshipService: FriendshipService,
              private messageService: MessageHandlerService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {

    if (this.router.url.includes('/timeline')) {
      this.showBar = false;
    }

    this.route.queryParams.subscribe(params => {
      const id = params.userId;

      this.userId = id
        ? Number(id)
        : this.currentUserId;

      this.loadFriends();
    });
  }

  loadFriends(): void {
    this.friendshipService.getUserFriends(this.userId).subscribe({
      next: (data) => {
        this.friends = data.map(f => ({
          ...f,
          isFriend: true,
          requestSent: false
        }));
      },
      error: (err) => {
        this.messageService.handleError(err);
      }
    });
  }



  unfriend(friend: Friendship): void {
    this.friendshipService.cancelFriendRequest(friend.friendId).subscribe({
      next: () => {
        friend.isFriend = false;
        friend.requestSent = false;
      },
      error: (err) => {
        this.messageService.handleError(err);
      }
    });
  }


  addFriend(friend: Friendship): void {
    this.friendshipService.sendFriendRequest(friend.id).subscribe({
      next: () => {
        friend.requestSent = true;
        friend.isFriend = false;
      },
      error: (err) => {
        this.messageService.handleError(err);
      }
    });
  }


}
