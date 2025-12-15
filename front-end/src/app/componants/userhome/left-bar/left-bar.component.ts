import { Component, OnInit } from '@angular/core';
import {FriendshipService} from '../../../../service/friendship.service';

@Component({
  selector: 'app-left-bar',
  templateUrl: './left-bar.component.html',
  styleUrls: ['./left-bar.component.css']
})
export class LeftBarComponent implements OnInit {

  username: string | null = null;
  friendsCount = 0;

  constructor(private friendshipService: FriendshipService) { }

  ngOnInit(): void {

    this.username = localStorage.getItem('username');

    this.friendshipService.getMyFriends().subscribe({
      next: (friends) => this.friendsCount = friends.length,
      error: (err) => console.error('Error fetching friends', err)
    });

  }

}
