import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Crime } from '../models/crime.model';
import { NotificationService } from './notification.service'; // Import NotificationService

@Injectable({
  providedIn: 'root',
})
export class CrimeService {
  private apiUrl = 'http://localhost:8080/api/complaints'; // Consistent API base

  constructor(
    private http: HttpClient,
    private notificationService: NotificationService // Inject NotificationService
  ) {}

  // Get all crimes
  getCrimes(): Observable<Crime[]> {
    return this.http.get<Crime[]>(this.apiUrl);
  }

  // Get a single crime by ID
  getCrimeById(id: number): Observable<Crime> {
    return this.http.get<Crime>(`${this.apiUrl}/${id}`);
  }

  // Add a new crime
  createCrime(userId: number, crime: Crime): Observable<Crime> {
    return this.http.post<Crime>(`${this.apiUrl}/file/${userId}`, crime);
  }

  // Update an existing crime
  updateCrime(crimeId: number, updatedCrime: Crime): Observable<any> {
    return this.http.put(`${this.apiUrl}/${crimeId}`, updatedCrime).pipe(
      tap(() => {
        // Call the method from NotificationService to trigger update
        this.notificationService.refreshNotifications(1); // This could be the userId that needs notification update
      })
    );
  }

  // Delete a crime
  deleteCrime(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Assign an officer to a crime
  assignOfficer(crimeId: number, officerId: number): Observable<void> {
    return this.http.put<void>(
      `${this.apiUrl}/${crimeId}/assign/${officerId}`,
      {}
    );
  }

  // Get crimes by user (for dashboard)
  getComplaintsByUser(userId: number, role: string): Observable<Crime[]> {
    return this.http.get<Crime[]>(
      `http://localhost:8080/api/complaints/user/${userId}?role=${role}`
    );
  }

  // Get complaint stats for the citizen dashboard
  getComplaintStats(userId: number) {
    return this.http.get<{ total: number; resolved: number; pending: number }>(
      `http://localhost:8080/api/complaints/user/stats/${userId}`
    );
  }
}
