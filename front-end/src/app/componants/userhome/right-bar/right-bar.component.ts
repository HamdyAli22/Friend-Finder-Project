import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import {FriendshipService} from '../../../../service/friendship.service';
import {AuthService} from '../../../../service/auth.service';

@Component({
  selector: 'app-right-bar',
  templateUrl: './right-bar.component.html',
  styleUrls: ['./right-bar.component.css']
})
export class RightBarComponent implements OnInit {

  suggestions: any[] = [];
  filteredSuggestions: any[] = [];

  @Input() searchKey: string = '';

  serverBase = 'http://localhost:8081';
  currentProfileImage?: string;

  constructor(private friendshipService: FriendshipService,
              private authService: AuthService) { }

  ngOnInit(): void {

    this.authService.profileImage$.subscribe(img => {
      this.currentProfileImage = img
        ? this.serverBase + img
        : 'assets/images/users/default-avatar-icon.jpg';
    });

    this.loadSuggestions();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['searchKey']) {
      this.filterSuggestions(this.searchKey);
    }
  }

  filterSuggestions(key: string): void {
    if (!key || !key.trim()) {
      this.filteredSuggestions = [...this.suggestions];
      return;
    }

    const lowerKey = key.toLowerCase();
    this.filteredSuggestions = this.suggestions.filter(u =>
      (u.username ?? '').toLowerCase().includes(lowerKey)
    );
  }

  loadSuggestions(): void {
    this.friendshipService.getSuggestions().subscribe(response => {
      this.suggestions = response;
      this.filterSuggestions(this.searchKey);
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
