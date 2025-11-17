import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { OfficersComponent } from './components/officers/officers.component';
import { CrimesComponent } from './components/view-crimes/view-crimes.component';
import { UsersComponent } from './components/users/users.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { CrimeDetailsComponent } from './components/crime-details/crime-details.component';
import { OfficerDetailsComponent } from './components/officer-details/officer-details.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';
import { CitizenDashboardComponent } from './components/citizen-dashboard/citizen-dashboard.component';
import { OfficerDashboardComponent } from './components/officer-dashboard/officer-dashboard.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { UpdateProfileComponent } from './components/update-profile/update-profile.component';
import { FileCrimeComponent } from './components/file-crime/file-crime.component';
import { NotificationPanelComponent } from './components/notification-panel/notification-panel.component';
import { FooterComponent } from './components/footer/footer.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'crimes', component: CrimesComponent, canActivate: [authGuard] },

  {
    path: 'citizen-dashboard',
    component: CitizenDashboardComponent,
    canActivate: [authGuard],
  },
  {
    path: 'officer-dashboard',
    component: OfficerDashboardComponent,
    canActivate: [authGuard],
  },
  {
    path: 'admin-dashboard',
    component: AdminDashboardComponent,
    canActivate: [authGuard],
  },

  {
    path: 'crimes/:id',
    component: CrimeDetailsComponent,
    canActivate: [authGuard],
  },
  { path: 'users', component: UsersComponent, canActivate: [adminGuard] },
  {
    path: 'profile',
    component: UserProfileComponent,
    canActivate: [authGuard],
  },
  { path: 'officers', component: OfficersComponent, canActivate: [adminGuard] },
  {
    path: 'officers/:id',
    component: OfficerDetailsComponent,
    canActivate: [authGuard],
  },
  {
    path: 'update-profile/:id',
    component: UpdateProfileComponent,
  },
  { path: 'file-crime/:id', component: FileCrimeComponent },
  {
    path: 'notification-panel',
    component: NotificationPanelComponent,
  },
  {
    path: 'footer',
    component: FooterComponent,
  },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
