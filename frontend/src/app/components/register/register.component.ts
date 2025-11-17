import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  constructor(private http: HttpClient, private router: Router) {}

  onSubmit(form: NgForm) {
    if (form.valid) {
      const userData = {
        username: form.value.username,
        password: form.value.password,
        email: form.value.email,
        firstname: form.value.firstname,
        lastname: form.value.lastname,
        role: 'CITIZEN',
      };

      console.log('Submitting:', userData);

      this.http
        .post('http://localhost:8080/api/users/register', userData)
        .subscribe({
          next: (response) => {
            console.log('Registration successful:', response);
            this.router.navigate(['/login']);
          },
          error: (err) => {
            console.error('Registration error:', err);
            alert(
              'Registration failed: ' + (err.error?.message || err.message)
            );
          },
        });
    }
  }
}
