import {Component, ElementRef, EventEmitter, HostListener, OnInit, Output} from '@angular/core';
import {AuthService} from '../../../service/auth.service';
import {Router} from '@angular/router';
import {NotificationService} from '../../../service/notification.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  unreadCount = 0;
  showNotifications = false;
  email: string;

  @Output() searchKey = new EventEmitter<string>();


  constructor(private router: Router,
              private authService: AuthService,
              private notificationService: NotificationService,
              private elRef: ElementRef) { }

  ngOnInit(): void {
    this.email = localStorage.getItem('email') || '';
    if (this.email) {
      this.loadUnreadCount();
      setInterval(() => this.loadUnreadCount(), 10000);
    }

    this.authService.userLoggedIn.subscribe(email => {
      this.email = email;
      this.loadUnreadCount();
      setInterval(() => this.loadUnreadCount(), 10000);
    });

  }

  loadUnreadCount(): void {
    if (!this.email) { return; }
    this.notificationService.getUnreadByUser(this.email).subscribe({
      next: (data) => (this.unreadCount = data.length),
      error: () => (this.unreadCount = 0)
    });
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }

  isUserLogin(): boolean{
    return this.authService.isUserLogin();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

  @HostListener('document:click', ['$event']) handleClickOutside = (event: Event) => {
    const clickedInside = this.elRef.nativeElement.contains(event.target);
    if (!clickedInside) {
      this.showNotifications = false;
    }
  }

  search(key: string): void {
    const trimmedKey = key.trim();

    let targetRoute = '/mainpage';

    if (this.router.url.includes('/timeline')) {
      targetRoute = '/timeline';
    }

    if (!trimmedKey) {
      this.router.navigate([targetRoute], { queryParams: {} });
      return;
    }

    this.router.navigate([targetRoute], {
      queryParams: { q: trimmedKey }
    });
  }



}
