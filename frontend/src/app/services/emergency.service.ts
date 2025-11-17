// src/app/services/emergency.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Emergency } from '../models/emergency.model'; // Correct import

@Injectable({
  providedIn: 'root',
})
export class EmergencyService {
  private baseUrl = 'http://localhost:8080/api/emergency';

  constructor(private http: HttpClient) {}

  reportEmergency(userId: number, location?: string): Observable<Emergency> {
    let url = `${this.baseUrl}?userId=${userId}`;
    if (location) url += `&location=${encodeURIComponent(location)}`;
    return this.http.post<Emergency>(url, {});
  }

  markAsResponded(id: number): Observable<Emergency> {
    return this.http.patch<Emergency>(`${this.baseUrl}/${id}/respond`, {});
  }

  getActiveEmergencies(): Observable<Emergency[]> {
    return this.http.get<Emergency[]>(`${this.baseUrl}/active`);
  }
}
