import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../../../service/auth.service';
import {Account} from '../../../../model/account';
import {FriendshipService} from '../../../../service/friendship.service';

@Component({
  selector: 'app-time-line-profile',
  templateUrl: './time-line-profile.component.html',
  styleUrls: ['./time-line-profile.component.css']
})
export class TimeLineProfileComponent implements OnInit {

  profile!: Account;
  currentUserId = +localStorage.getItem('userId');
  isFriend = false;
  requestSent = false;
  pendingFriendshipId?: number;

  constructor( private route: ActivatedRoute,
               private authService: AuthService,
               private friendshipService: FriendshipService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const userId = params.userId;

      this.loadProfile(userId ? +userId : undefined);
    });
  }

  private loadProfile(userId?: number): void {
    this.authService.getProfile(userId).subscribe(res => {
      this.profile = res;
      this.checkFriendStatus();
    });
  }

  checkFriendStatus = () => {
    if (this.profile.id === this.currentUserId) {
      this.isFriend = false;
      this.requestSent = false;
      this.pendingFriendshipId = undefined;
      return;
    }

    this.friendshipService.getMyFriends().subscribe(friends => {
      this.isFriend = friends.some(f => f.id === this.profile.id);
    });

    this.friendshipService.getPendingSentRequests().subscribe(requests => {
      // this.requestSent = requests.some(r => r.id === this.profile.id);
      const pending = requests.find(r => r.id === this.profile.id);
      if (pending) {
        this.requestSent = true;
        this.pendingFriendshipId = pending.friendId;
      } else {
        this.requestSent = false;
        this.pendingFriendshipId = undefined;
      }
    });
  }

  addFriend = () => {
    this.friendshipService.sendFriendRequest(this.profile.id).subscribe(() => {
      this.requestSent = true;
      this.isFriend = false;
      this.loadProfile(this.profile.id);
    });
  }

  cancelRequest(): void {
    if (!this.pendingFriendshipId) { return; }
    this.friendshipService.cancelFriendRequest(this.pendingFriendshipId).subscribe(() => {
      this.requestSent = false;
      this.pendingFriendshipId = undefined;
    });
  }

}
