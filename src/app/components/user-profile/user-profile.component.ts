import { Component, OnInit } from '@angular/core';
import { User } from '../../models/user.model'; // Correct path
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  user: User | undefined;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe((user: User) => {
      this.user = user;
    });
  }
}
