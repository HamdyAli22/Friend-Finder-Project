import { Component, OnInit } from '@angular/core';
import {FriendshipService} from '../../../../service/friendship.service';
import {AuthService} from '../../../../service/auth.service';

@Component({
  selector: 'app-left-bar',
  templateUrl: './left-bar.component.html',
  styleUrls: ['./left-bar.component.css']
})
export class LeftBarComponent implements OnInit {

  username: string | null = null;
  friendsCount = 0;
  profileImage?: string;
  private serverBase = 'http://localhost:8081';

  constructor(private friendshipService: FriendshipService,
              private authService: AuthService) { }

  ngOnInit(): void {

    this.username = localStorage.getItem('username');

    this.authService.profileImage$.subscribe(img => {
      this.profileImage = img
        ? this.serverBase + img
        : 'assets/images/users/default-avatar-icon.jpg';
    });

    this.friendshipService.getMyFriends().subscribe({
      next: (friends) => this.friendsCount = friends.length,
      error: (err) => console.error('Error fetching friends', err)
    });

  }

}
