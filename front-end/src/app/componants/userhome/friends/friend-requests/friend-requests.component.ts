import { Component, OnInit } from '@angular/core';
import {FriendshipService} from '../../../../../service/friendship.service';

@Component({
  selector: 'app-friend-requests',
  templateUrl: './friend-requests.component.html',
  styleUrls: ['./friend-requests.component.css']
})
export class FriendRequestsComponent implements OnInit {

  friendRequests: any[] = [];
  sentRequests: any[] = [];
  showSentPopup = false;

  constructor(private friendshipService: FriendshipService) { }

  ngOnInit(): void {
    this.loadFriendRequests();
  }

  loadFriendRequests(): void {
    this.friendshipService.getPendingReceivedRequests().subscribe(response => {
      this.friendRequests = response;
    });
  }

  acceptRequest(request: any): void {
    this.friendshipService.acceptFriendRequest(request.friendId).subscribe(() => {
      this.friendRequests = this.friendRequests.filter(r => r.id !== request.id);
    });
  }

  rejectRequest(request: any): void {
    this.friendshipService.rejectFriendRequest(request.friendId).subscribe(() => {
      this.friendRequests = this.friendRequests.filter(r => r.id !== request.id);
    });
  }

  openSentRequests(): void {
    this.showSentPopup = true;
    this.friendshipService.getPendingSentRequests().subscribe(response => {
      this.sentRequests = response;
    });
  }

  closeSentRequests(): void {
    this.showSentPopup = false;
  }

  cancelRequest(request: any): void {
    this.friendshipService
      .cancelFriendRequest(request.friendId)
      .subscribe(() => {
        this.sentRequests =
          this.sentRequests.filter(r => r.friendId !== request.friendId);
      });
  }

}
