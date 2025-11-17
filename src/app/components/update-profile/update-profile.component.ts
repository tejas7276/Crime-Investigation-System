import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css'],
})
export class UpdateProfileComponent implements OnInit {
  user: any = {};
  showPassword: boolean = false;
  toastMessage: string = '';
  toastType: 'success' | 'error' | '' = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    const currentUser = JSON.parse(localStorage.getItem('user') || '{}');
    this.user = { ...currentUser };
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  showToast(message: string, type: 'success' | 'error') {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => {
      this.toastMessage = '';
      this.toastType = '';
    }, 3000); // disappears in 3 seconds
  }

  updateProfile() {
    console.log('📦 Sending this.user:', this.user);

    this.userService.updateUser(this.user).subscribe({
      next: (res) => {
        this.showToast('Profile updated successfully!', 'success');
        localStorage.setItem('user', JSON.stringify(this.user));
        alert('✅ User Details Updated Successfully.!!');
      },
      error: (err) => {
        this.showToast(err.message || 'Update failed', 'error');
      },
    });
  }
}
