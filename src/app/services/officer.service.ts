import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Officer } from '../models/officer.model'; // ✅ Import Officer model

@Injectable({
  providedIn: 'root',
})
export class OfficerService {
  private apiUrl = 'http://localhost:8080/api/officers'; // ✅ Ensure consistent API path

  constructor(private http: HttpClient) {}

  // ✅ Get all officers with type safety
  getAllOfficers(): Observable<Officer[]> {
    return this.http.get<Officer[]>(this.apiUrl);
  }

  // ✅ Get officer by ID
  getOfficerById(id: number): Observable<Officer> {
    return this.http.get<Officer>(`${this.apiUrl}/${id}`);
  }

  // ✅ Add new officer
  addOfficer(officer: Officer): Observable<Officer> {
    return this.http.post<Officer>(this.apiUrl, officer);
  }

  // ✅ Delete officer
  deleteOfficer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
