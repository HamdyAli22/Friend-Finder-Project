import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {NotificationService} from '../../../service/notification.service';
import {Notification} from '../../../model/notification';
import {Router, NavigationStart} from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {

  notifications: Notification[] = [];
  email: string = localStorage.getItem('email') || '';
  loading = false;
  @Output() closed = new EventEmitter<void>();
  @Output() unreadCountChanged = new EventEmitter<number>();
  activeTab: 'all' | 'unread' = 'all';
  unreadCount = 0;
  private routerSub!: Subscription;
  showHeaderMenu = false;
  headerDropdownStyle: any = {};

  constructor(private notificationService: NotificationService,
              private router: Router) { }

  ngOnInit(): void {
    if (this.email) {
      this.loadNotifications();
    }

    this.routerSub = this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.closeDropdown();
      }
    });
  }

  loadNotifications(): void {
    this.loading = true;
    this.notificationService.getAllByUser(this.email).subscribe({
      next: (data) => {
        this.notifications = data.sort((a, b) => (b.id ?? 0) - (a.id ?? 0));
        this.updateUnreadCount();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.notifications = [];
      }
    });
  }

  filteredNotifications(): Notification[] {
    return this.activeTab === 'unread'
      ? this.notifications.filter(n => !n.read)
      : this.notifications;
  }

  markAsRead(notification: Notification): void {
    if (!notification.read) {
      notification.read = true;
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => {
          this.updateUnreadCount();
        },
        error: () => {
          notification.read = false;
          this.updateUnreadCount();
        }
      });
    }
  }

  onNotificationClick(notification: Notification): void {
    this.markAsRead(notification);

    // 👇 1) لو إشعار عليه بوست
    if (notification.postId) {
      // نروح على التايم لاين ونسيب TimeLineComponent يتعامل
      this.router.navigate(['/timeline'], {
        queryParams: { postId: notification.postId }
      });
    }

    // 👇 2) طلب صداقة
    else if (notification.type === 'NEW_REQUEST') {
      this.router.navigate(['/friend-requests']);
    }

    // 👇 3) قبول طلب صداقة
    else if (notification.type === 'ACCEPT_REQUEST') {
      this.router.navigate(['/friends']);
    }

    notification.showMenu = false;
  }


  updateUnreadCount(): void {
    this.unreadCount = this.notifications.filter(n => !n.read).length;
    this.unreadCountChanged.emit(this.unreadCount);
  }
  setActiveTab(tab: 'all' | 'unread'): void {
    this.activeTab = tab;
    // if (tab === 'unread') {
    //   this.notifications.forEach(n => n.read = true);
    //   this.updateUnreadCount(); // هيخلي البادج تختفي تلقائيًا
    // }
  }

  getTimeAgo(dateStr: string): string {
    const now = new Date();
    const past = new Date(dateStr);
    const diffMs = now.getTime() - past.getTime();

    const seconds = Math.floor(diffMs / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);

    if (days > 0) { return `${days}d`; }
    if (hours > 0) { return `${hours}h`; }
    if (minutes > 0) { return `${minutes}m`; }
    return `${seconds}s`;
  }


  closeDropdown(): void {
    this.closed.emit();
  }

  onMarkAsReadClick(event: MouseEvent, notification: Notification): void {
    event.preventDefault();
    event.stopPropagation(); // ✅ يمنع الانتقال للصفحة
    this.markAsRead(notification); // ✅ يخليها مقرؤة فقط
    notification.showMenu = false;
  }

  onDeleteClick(event: MouseEvent, notification: Notification): void {
    event.preventDefault();
    event.stopPropagation(); // يمنع التنقل للصفحة

    // حذف الإشعار من السيرفر
    this.notificationService.deleteNotification(notification.id).subscribe({
      next: () => {
        // ✅ إزالة الإشعار محليًا من المصفوفة
        this.notifications = this.notifications.filter(n => n.id !== notification.id);
        this.updateUnreadCount();

        // ✅ يقفل القائمة المنسدلة
        this.closeDropdownMenu(event);
      },
      error: err => {
        console.error('Error deleting notification', err);
      }
    });
  }

  // private closeDropdownMenu(event: MouseEvent): void {
  //   const dropdownMenu = (event.target as HTMLElement).closest('.dropdown-menu');
  //   if (dropdownMenu) {
  //     dropdownMenu.classList.remove('show');
  //   }
  // }

  private closeDropdownMenu(event: MouseEvent): void {
    const dropdown = (event.target as HTMLElement).closest('.dropdown');
    if (dropdown) {
      const btn = dropdown.querySelector('[data-bs-toggle="dropdown"]') as HTMLElement;
      if (btn && btn.getAttribute('aria-expanded') === 'true') {
        btn.click(); // ✅ يغلق القائمة بطريقة Bootstrap الرسمية
      }
    }
  }


  ngOnDestroy = (): void => {
    if (this.routerSub) {
      this.routerSub.unsubscribe();
    }
  }

   markAllAsRead(event?: MouseEvent): void {
    if (event) {
      event.stopPropagation();
    }

    this.notifications.forEach(n => n.read = true);
    this.updateUnreadCount();

    this.notificationService.markAllAsRead(this.email).subscribe();
    this.showHeaderMenu = false;
  }

  toggleHeaderMenu(event: MouseEvent): void {
    event.stopPropagation();
    this.showHeaderMenu = !this.showHeaderMenu;
    if(this.showHeaderMenu) {
      const btn = event.currentTarget as HTMLElement;
      const rect = btn.getBoundingClientRect();
      this.headerDropdownStyle = {
        top: `${rect.bottom + 4}px`,
        left: `${rect.right - 180}px` // 180px عرض dropdown الهيدر
      };
    }
  }

  toggleItemMenu(event: MouseEvent, n: any) {
    event.stopPropagation();

    // اقفل أي Dropdown مفتوح
    this.notifications.forEach(x => {
      if (x !== n) x.showMenu = false;
    });

    n.showMenu = !n.showMenu;

    if (n.showMenu) {
      const btn = event.currentTarget as HTMLElement;
      const rect = btn.getBoundingClientRect();

      // نحسب مكان الـ dropdown بالنسبة للشاشة
      n.dropdownStyle = {
        top: `${rect.bottom + window.scrollY}px`,
        left: `${rect.right - 200}px` // 200px عرض الـ dropdown
      };
    }
  }


}
