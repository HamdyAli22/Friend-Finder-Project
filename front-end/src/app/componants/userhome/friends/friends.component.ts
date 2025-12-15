import { Component, OnInit } from '@angular/core';
import {Friendship} from '../../../../model/friendship';
import {FriendshipService} from '../../../../service/friendship.service';
import {MessageHandlerService} from '../../../../service/message-handler.service';

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent implements OnInit {

  friends: Friendship[] = [];

  constructor(private friendshipService: FriendshipService,
              private messageService: MessageHandlerService) { }

  ngOnInit(): void {
    this.loadFriends();
  }

  loadFriends(): void {
    this.friendshipService.getMyFriends().subscribe({
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
