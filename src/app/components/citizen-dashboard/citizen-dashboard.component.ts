import { Component, OnInit, OnDestroy } from '@angular/core';
import { CrimeService } from 'src/app/services/crime.service';
import { Crime } from 'src/app/models/crime.model';
import { EmergencyService } from 'src/app/services/emergency.service';

@Component({
  selector: 'app-citizen-dashboard',
  templateUrl: './citizen-dashboard.component.html',
  styleUrls: ['./citizen-dashboard.component.css'],
})
export class CitizenDashboardComponent implements OnInit, OnDestroy {
  // User Data
  firstname = 'User';
  userId = 0;
  role = '';

  // Crime Data
  isLoading = true;
  hasError = false;
  userCrimes: Crime[] = [];
  resolvedCrimes: Crime[] = [];
  pendingCrimes: Crime[] = [];
  stats = { total: 0, resolved: 0, pending: 0 };

  // Modal
  showModal = false;
  modalTitle = '';
  modalCrimes: Crime[] = [];

  // SOS Emergency
  isSOSLoading = false;
  isSOSActive = false;
  sosLocation = '';
  showLocationInput = false;
  notification = '';
  private sosTimer: any;

  constructor(
    private crimeService: CrimeService,
    private emergencyService: EmergencyService
  ) {}

  ngOnInit(): void {
    this.loadUserData();
  }

  ngOnDestroy(): void {
    if (this.sosTimer) clearTimeout(this.sosTimer);
  }

  // SOS Functions
  triggerEmergencySOS(): void {
    if (this.isSOSActive) {
      if (confirm('Cancel emergency request?')) {
        this.cancelEmergency();
      }
      return;
    }
    this.showLocationInput = true;
  }

  confirmSOS(): void {
    if (!this.sosLocation.trim()) {
      alert('Please enter your location');
      return;
    }

    this.isSOSLoading = true;
    this.emergencyService
      .reportEmergency(this.userId, this.sosLocation)
      .subscribe({
        next: () => {
          this.isSOSLoading = false;
          this.isSOSActive = true;
          this.showLocationInput = false;
          this.notification = 'Help is coming! Stay safe.';

          // Auto reset after 30 seconds
          this.sosTimer = setTimeout(() => {
            this.isSOSActive = false;
            this.notification = '';
          }, 30000);
        },
        error: () => {
          this.isSOSLoading = false;
          this.notification = 'Failed to send alert. Please try again.';
        },
      });
  }

  cancelSOS(): void {
    this.showLocationInput = false;
    this.sosLocation = '';
  }

  private cancelEmergency(): void {
    this.isSOSActive = false;
    if (this.sosTimer) clearTimeout(this.sosTimer);
    this.notification = 'Emergency cancelled.';
  }
  // ======================
  // EXISTING CRIME FEATURES
  // ======================

  loadUserData(): void {
    try {
      const userData = localStorage.getItem('user');
      if (!userData) throw new Error('No user data found');

      const user = JSON.parse(userData);
      if (!user?.id || !user?.role) throw new Error('Invalid user data');

      this.firstname = user.firstname || 'User';
      this.userId = user.id;
      this.role = user.role;

      this.loadCrimeData();
    } catch (error) {
      console.error('User data error:', error);
      this.handleError();
    }
  }

  loadCrimeData(): void {
    this.isLoading = true;
    this.hasError = false;

    this.crimeService.getComplaintsByUser(this.userId, this.role).subscribe({
      next: (crimes) => {
        this.processCrimeData(crimes);
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Crime data error:', err);
        this.handleError();
      },
    });
  }

  processCrimeData(crimes: any): void {
    if (!Array.isArray(crimes)) {
      throw new Error('Invalid crime data format');
    }

    this.userCrimes = crimes;
    this.resolvedCrimes = crimes.filter(
      (c) => c.status?.toLowerCase() === 'resolved'
    );
    this.pendingCrimes = crimes.filter(
      (c) => c.status?.toLowerCase() !== 'resolved'
    );

    this.updateStats();
  }

  updateStats(): void {
    this.stats = {
      total: this.userCrimes.length,
      resolved: this.resolvedCrimes.length,
      pending: this.pendingCrimes.length,
    };
  }

  handleError(): void {
    this.userCrimes = [];
    this.resolvedCrimes = [];
    this.pendingCrimes = [];
    this.updateStats();
    this.isLoading = false;
    this.hasError = true;
  }

  openModal(title: string, crimes: Crime[]): void {
    this.modalTitle = title;
    this.modalCrimes = crimes || [];
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }
}
