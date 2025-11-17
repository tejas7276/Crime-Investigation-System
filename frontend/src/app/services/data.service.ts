import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrl = '/api'; // Use the proxy path

  constructor(private http: HttpClient) {}

  // Example: Fetch data from the backend
  getData() {
    return this.http.get(`${this.apiUrl}/endpoint`); // Replace `/endpoint` with your actual API endpoint
  }

  // Example: Send data to the backend
  postData(data: any) {
    return this.http.post(`${this.apiUrl}/endpoint`, data);
  }
}
