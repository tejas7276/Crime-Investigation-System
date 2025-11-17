import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { NotificationService } from '../../services/notification.service';
import { Notification } from '../../models/notification.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-notification-panel',
  templateUrl: './notification-panel.component.html',
  styleUrls: ['./notification-panel.component.css'],
  providers: [DatePipe],
})
export class NotificationPanelComponent implements OnInit {
  @Input() showPanel: boolean = false;
  @Output() panelClosed = new EventEmitter<void>();

  notifications: Notification[] = [];
  unreadCount: number = 0;
  isLoading: boolean = false;
  userId: number = 1;

  constructor(
    private notificationService: NotificationService,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.userId = Number(localStorage.getItem('userId')) || 1;
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.isLoading = true;
    this.notificationService.getUserNotifications(this.userId).subscribe({
      next: (response: Notification[] | { data: Notification[] }) => {
        // Handle both array response and wrapped response
        const notifications = this.extractNotifications(response);

        this.notifications = notifications.map((notif) => ({
          ...notif,
          timestamp:
            this.datePipe.transform(notif.timestamp, 'medium') ||
            notif.timestamp,
        }));

        this.updateUnreadCount();
        this.isLoading = false;
      },
      error: (error: any) => {
        this.isLoading = false;
        console.error('Error loading notifications:', error);
      },
    });
  }

  private extractNotifications(
    response: Notification[] | { data: Notification[] }
  ): Notification[] {
    if (Array.isArray(response)) {
      return response;
    }
    if (response && Array.isArray(response.data)) {
      return response.data;
    }
    return [];
  }

  private updateUnreadCount(): void {
    this.unreadCount = this.notifications.filter(
      (n) => n.status === 'unread'
    ).length;
  }

  markAsRead(notificationId: number): void {
    this.notificationService.markAsRead(notificationId).subscribe({
      next: () => {
        const notification = this.notifications.find(
          (n) => n.id === notificationId
        );
        if (notification && notification.status !== 'read') {
          notification.status = 'read';
          this.updateUnreadCount();
        }
      },
      error: (error: any) => {
        console.error('Error marking notification as read:', error);
      },
    });
  }

  closePanel(): void {
    this.panelClosed.emit();
  }
}
