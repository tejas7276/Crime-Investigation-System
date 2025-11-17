import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { OfficersComponent } from './components/officers/officers.component';
import { CrimesComponent } from './components/view-crimes/view-crimes.component';
import { UsersComponent } from './components/users/users.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { CrimeDetailsComponent } from './components/crime-details/crime-details.component';
import { OfficerDetailsComponent } from './components/officer-details/officer-details.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { FormsModule } from '@angular/forms';
import { FooterComponent } from './components/footer/footer.component';
import { CitizenDashboardComponent } from './components/citizen-dashboard/citizen-dashboard.component';
import { OfficerDashboardComponent } from './components/officer-dashboard/officer-dashboard.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { UpdateProfileComponent } from './components/update-profile/update-profile.component';
import { FileCrimeComponent } from './components/file-crime/file-crime.component';
import { NotificationPanelComponent } from './components/notification-panel/notification-panel.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { NotificationService } from './services/notification.service';

@NgModule({
  declarations: [
    AppComponent,
    OfficersComponent,
    CrimesComponent,
    UsersComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    CrimeDetailsComponent,
    OfficerDetailsComponent,
    UserProfileComponent,
    FooterComponent,
    CitizenDashboardComponent,
    OfficerDashboardComponent,
    AdminDashboardComponent,
    UpdateProfileComponent,
    FileCrimeComponent,
    NotificationPanelComponent,
    NavbarComponent,
  ],
  imports: [
    BrowserModule,
    CommonModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
  ],
  providers: [NotificationService],
  bootstrap: [AppComponent],
})
export class AppModule {}
