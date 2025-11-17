import { Component, HostListener, OnInit } from '@angular/core';
import { NotificationService } from '../../services/notification.service';
import { Notification } from '../../models/notification.model';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  notifications: Notification[] = [];
  unreadNotifications: Notification[] = [];
  showNotifications = false;
  unreadCount = 0;
  userId: number | null = null; // Changed to null initially
  isLoading = false;
  error: string | null = null;

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.getUserId(); // First get the user ID
    this.setupNotificationUpdates();
  }

  // Properly get user ID from localStorage
  getUserId(): void {
    const storedId = localStorage.getItem('userId');
    if (storedId) {
      this.userId = parseInt(storedId, 10);
      this.loadNotifications(); // Only load notifications if we have a user ID
    } else {
      console.error('No user ID found in localStorage');
    }
  }

  // Listen for updates to notifications and reload them when updated
  setupNotificationUpdates(): void {
    this.notificationService.notificationUpdate$.subscribe(() => {
      if (this.userId) {
        // Only load if we have a user ID
        this.loadNotifications();
      }
    });
  }

  // Function to load notifications for the user
  loadNotifications(): void {
    if (!this.userId) return; // Don't load if no user ID

    this.isLoading = true;
    this.notificationService.getUserNotifications(this.userId).subscribe(
      (response: any) => {
        const notifications = Array.isArray(response)
          ? response
          : Array.isArray(response?.data)
          ? response.data
          : [];

        this.notifications = notifications;
        this.filterUnreadNotifications();
        this.isLoading = false;
      },
      (error) => {
        this.isLoading = false;
        this.error = 'Error loading notifications';
        console.error(error);
      }
    );
  }

  // Rest of your existing methods remain the same...
  filterUnreadNotifications(): void {
    this.unreadNotifications = this.notifications.filter(
      (n) => n.status === 'unread'
    );
    this.unreadCount = this.unreadNotifications.length;
  }

  onNotificationToggle(): void {
    this.showNotifications = !this.showNotifications;
    if (this.showNotifications && this.userId) {
      this.loadNotifications();
    }
  }

  markAsRead(notificationId: number): void {
    this.isLoading = true;
    this.notificationService.markAsRead(notificationId).subscribe(
      () => {
        const notification = this.notifications.find(
          (n) => n.id === notificationId
        );
        if (notification && notification.status !== 'read') {
          notification.status = 'read';
          this.filterUnreadNotifications();
        }
        this.isLoading = false;
      },
      (error) => {
        this.isLoading = false;
        this.error = 'Failed to mark as read';
        console.error('Error marking notification as read:', error);
      }
    );
  }

  logout(): void {
    localStorage.clear();
    window.location.href = '/login';
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.notification-wrapper')) {
      this.showNotifications = false;
    }
  }
}
