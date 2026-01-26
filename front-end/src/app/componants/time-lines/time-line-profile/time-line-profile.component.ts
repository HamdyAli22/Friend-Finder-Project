import {Component, ElementRef, HostListener, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../../../service/auth.service';
import {Account} from '../../../../model/account';
import {FriendshipService} from '../../../../service/friendship.service';
import {AccountService} from '../../../../service/account.service';
import {FileUploadService} from '../../../../service/file-upload.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

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
  editUsername = false;
  editBio = false;
  previewProfileImage?: SafeUrl;
  previewCoverImage?: string;

  selectedProfileFile?: File;
  selectedCoverFile?: File;

  profileImageChanged = false;
  coverImageChanged = false;

  userId!: number;

  private serverBase = 'http://localhost:8081';

  constructor( private route: ActivatedRoute,
               private authService: AuthService,
               private accountService : AccountService,
               private friendshipService: FriendshipService,
               private elRef: ElementRef,
               private fileUploadService: FileUploadService,
               private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const id  = params.userId;
      this.userId = id ? +id : this.currentUserId;
      this.loadProfile(this.userId);
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

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: Event) {
    const clickedInside = this.elRef.nativeElement.contains(event.target);
    if (!clickedInside && (this.editUsername || this.editBio)) {
      this.cancelEdit();
    }
  }

  toggleEdit(field: 'username' | 'bio') {
    if (field === 'username') this.editUsername = !this.editUsername;
    if (field === 'bio') this.editBio = !this.editBio;
  }

  cancelEdit() {
    this.editUsername = false;
    this.editBio = false;
    this.loadProfile(this.profile.id);
  }

  saveBasicInfo() {
    this.accountService.updateBasicInfo(this.profile).subscribe({
      next: updated => {
        this.profile = {
          ...this.profile,
          ...updated,
          friendsCount: this.profile.friendsCount // ✅ نحافظ عليه
        };
        this.editUsername = false;
        this.editBio = false;
        this.authService.setUsername(this.profile.username);
      },
      error: err => console.error(err)
    });
  }


  onProfileSelected(event: any) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.selectedProfileFile = file;
    this.previewProfileImage =
      this.sanitizer.bypassSecurityTrustUrl(
        URL.createObjectURL(file)
      );

    this.profileImageChanged = true;

    input.value = '';
  }



  onCoverSelected(event: any) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.selectedCoverFile = file;
    this.previewCoverImage = URL.createObjectURL(file);
    this.coverImageChanged = true;

    input.value = '';
  }



  getProfileImageUrl(): string | SafeUrl {
    if (this.previewProfileImage) {
      return this.previewProfileImage;
    }

    if (this.profile?.profileImageUrl) {
      return this.serverBase + this.profile.profileImageUrl;
    }

    return 'assets/images/users/default-avatar-icon.jpg';
  }

  getCoverImageUrl(): string {
    if (this.previewCoverImage) {
      return this.previewCoverImage;
    }

    if (this.profile?.coverImageUrl) {
      return this.serverBase + this.profile.coverImageUrl;
    }

    return 'assets/images/covers/default-cover.jpg';
  }


  saveProfileImage() {
    if (!this.selectedProfileFile) return;

    const userId = localStorage.getItem('userId');
    const folder = `users/${userId}/profile`;

    this.fileUploadService.uploadFile(this.selectedProfileFile, folder)
      .subscribe(res => {
        this.profile.profileImageUrl = res.path;
        this.authService.setProfileImage(res.path);
        this.resetProfileImagePreview();
        this.saveBasicInfo();
      });
  }

  saveCoverImage() {
    if (!this.selectedCoverFile) return;

    const userId = localStorage.getItem('userId');
    const folder = `users/${userId}/cover`;

    this.fileUploadService.uploadFile(this.selectedCoverFile, folder)
      .subscribe(res => {
        this.profile.coverImageUrl = res.path;
        this.resetCoverImagePreview();
        this.saveBasicInfo();
      });
  }

  cancelProfileImage() {
    this.resetProfileImagePreview();
  }

  cancelCoverImage() {
    this.resetCoverImagePreview();
  }

  private resetProfileImagePreview() {
    if (this.previewProfileImage) {
      URL.revokeObjectURL(this.previewProfileImage as any);
    }
    this.previewProfileImage = undefined;
    this.selectedProfileFile = undefined;
    this.profileImageChanged = false;
  }


  private resetCoverImagePreview() {
    if (this.previewCoverImage) {
      URL.revokeObjectURL(this.previewCoverImage);
    }
    this.previewCoverImage = undefined;
    this.selectedCoverFile = undefined;
    this.coverImageChanged = false;
  }

  deleteProfileImage() {
    this.profile.profileImageUrl = null as any;

    this.accountService.updateBasicInfo(this.profile).subscribe({
      next: updated => {
        this.profile = {
          ...this.profile,
          ...updated,
          friendsCount: this.profile.friendsCount
        };

        this.authService.setProfileImage(null);

        this.resetProfileImagePreview();
      },
      error: err => console.error(err)
    });
  }


  deleteCoverImage() {
    this.profile.coverImageUrl = null as any;

    this.accountService.updateBasicInfo(this.profile).subscribe({
      next: updated => {
        this.profile = {
          ...this.profile,
          ...updated,
          friendsCount: this.profile.friendsCount
        };
        this.resetCoverImagePreview();
      },
      error: err => console.error(err)
    });
  }

}
