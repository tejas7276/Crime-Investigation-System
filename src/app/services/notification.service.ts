import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Notification } from '../models/notification.model';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  notifications$ = this.notificationsSubject.asObservable();

  private unreadCountSubject = new BehaviorSubject<number>(0);
  unreadCount$ = this.unreadCountSubject.asObservable();

  private notificationUpdateSubject = new Subject<void>();
  notificationUpdate$ = this.notificationUpdateSubject.asObservable();

  constructor(private http: HttpClient) {}

  getUserNotifications(userId: number): Observable<Notification[]> {
    return this.http
      .get<Notification[]>(
        `http://localhost:8080/api/notifications/user/${userId}`
      )
      .pipe(
        tap((notifications) => {
          this.notificationsSubject.next(notifications);
          this.updateUnreadCount(notifications);
        })
      );
  }

  refreshNotifications(userId: number): void {
    this.getUserNotifications(userId).subscribe((notifications) => {
      this.notificationsSubject.next(notifications);
      this.notificationUpdateSubject.next();
    });
  }

  markAsRead(notificationId: number): Observable<Notification> {
    return this.http
      .put<Notification>(
        `http://localhost:8080/api/notifications/mark-as-read/${notificationId}`,
        {}
      )
      .pipe(
        tap(() => {
          this.notificationUpdateSubject.next();
        })
      );
  }

  getUnreadCount(userId: number): Observable<number> {
    return this.http
      .get<number>(
        `http://localhost:8080/api/notifications/user/${userId}/unread-count`
      )
      .pipe(
        tap((count) => {
          this.unreadCountSubject.next(count);
        })
      );
  }

  private updateUnreadCount(notifications: Notification[]): void {
    const unreadCount = notifications.filter(
      (n) => n.status === 'unread'
    ).length;
    this.unreadCountSubject.next(unreadCount);
  }
}
