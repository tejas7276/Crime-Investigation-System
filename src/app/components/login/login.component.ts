import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { HttpErrorResponse } from '@angular/common/http';

interface User {
  id: number;
  username: string;
  email: string;
  role: string;
  firstname: string;
  lastname?: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  credentials = { username: '', password: '' };
  errorMessage: string = '';
  isLoading: boolean = false;

  constructor(private userService: UserService, private router: Router) {}

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';

    this.userService
      .login(this.credentials.username, this.credentials.password)
      .subscribe({
        next: (user: User) => {
          localStorage.setItem('user', JSON.stringify(user));
          localStorage.setItem('userId', String(user.id)); // ✅ Added line to fix navbar notifications
          this.redirectUser(user.role);
        },
        error: (err: HttpErrorResponse) => {
          this.errorMessage = typeof err === 'string' ? err : 'Login failed';
          this.isLoading = false;
        },
        complete: () => (this.isLoading = false),
      });
  }

  private redirectUser(role: string): void {
    const routes: { [key: string]: string } = {
      ADMIN: '/admin-dashboard',
      OFFICER: '/officer-dashboard',
      CITIZEN: '/citizen-dashboard',
    };

    this.router.navigate([routes[role] || '/home']);
  }
}
