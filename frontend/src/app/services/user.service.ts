import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  // ✅ Get user by username
  getUserByUsername(username: string): Observable<any> {
    return this.http.get(
      `${this.apiUrl}/users/username/${username}`,
      this.getHttpOptions()
    );
  }

  // ✅ Update user with auth header
  updateUser(user: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/updateUser/${user.id}`, user);
  }

  // ✅ Get all users
  getAllUsers(): Observable<User[]> {
    return this.http
      .get<User[]>(this.apiUrl, this.getHttpOptions())
      .pipe(catchError(this.handleError));
  }

  // ✅ Get current user
  getCurrentUser(): Observable<User> {
    return this.http
      .get<User>(`${this.apiUrl}/me`, this.getHttpOptions())
      .pipe(catchError(this.handleError));
  }

  // ✅ Login and save auth token
  // user.service.ts
  login(username: string, password: string): Observable<User> {
    return this.http
      .post<User>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error.error?.message || 'Login failed');
        })
      );
  }
  // ✅ Register a new user
  register(userData: {
    username: string;
    password: string;
    email: string;
    firstname: string;
    lastname: string;
  }): Observable<any> {
    return this.http
      .post(`${this.apiUrl}/register`, userData, {
        responseType: 'text' as 'json',
      })
      .pipe(catchError(this.handleError));
  }

  // ✅ Logout and clear storage
  logout(): void {
    localStorage.removeItem('user');
    localStorage.removeItem('auth');
  }

  // ✅ Save user details
  saveUserDetails(user: {
    id: number;
    username: string;
    email: string;
    role: string;
    firstname: string;
    lastname: string;
  }): void {
    localStorage.setItem('user', JSON.stringify(user));
  }

  // ✅ Get user details
  getUserDetails(): any {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  // ✅ Check if user is logged in
  isLoggedIn(): boolean {
    return !!localStorage.getItem('user');
  }

  // ✅ Get user role
  getUserRole(): string | null {
    const user = this.getUserDetails();
    return user ? user.role : null;
  }

  // ✅ Add Basic Auth header
  private getHttpOptions() {
    const token = localStorage.getItem('auth');
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Basic ${token}`,
      }),
    };
  }

  // ✅ Handle errors
  private handleError(error: HttpErrorResponse) {
    console.error('❌ An error occurred:', error);
    let errorMessage = 'Something went wrong. Please try again later.';
    if (error.status === 401) {
      errorMessage = 'Invalid username or password.';
    } else if (error.status === 403) {
      errorMessage = 'You are not authorized to access this resource.';
    } else if (error.status === 400) {
      errorMessage = 'Bad request. Please check your input.';
    }
    return throwError(() => new Error(errorMessage));
  }
}
