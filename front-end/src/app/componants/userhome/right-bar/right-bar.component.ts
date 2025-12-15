import { Component, OnInit } from '@angular/core';
import {FriendshipService} from '../../../../service/friendship.service';

@Component({
  selector: 'app-right-bar',
  templateUrl: './right-bar.component.html',
  styleUrls: ['./right-bar.component.css']
})
export class RightBarComponent implements OnInit {

  suggestions: any[] = [];

  constructor(private friendshipService: FriendshipService) { }

  ngOnInit(): void {
    this.loadSuggestions();
  }

  loadSuggestions(): void {
    this.friendshipService.getSuggestions().subscribe(response => {
      this.suggestions = response;
    });
  }


  sendFriendRequest(user: any): void {
    this.friendshipService.sendFriendRequest(user.id).subscribe({
      next: (friendship) => {
        user.friendId = friendship.id;
      },
      error: () => {
        user.friendId = null;
      }
    });
  }

  cancelFriendRequest(user: any): void {
    if (!user.friendId) { return; }
    this.friendshipService.cancelFriendRequest(user.friendId).subscribe({
      next: () => {
        user.friendId = null;
      }
    });
  }

}
