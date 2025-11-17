import { Component, OnInit } from '@angular/core';
import * as L from 'leaflet';
import { OpenStreetMapProvider, GeoSearchControl } from 'leaflet-geosearch';
import { CrimeService } from 'src/app/services/crime.service';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-file-crime',
  templateUrl: './file-crime.component.html',
  styleUrls: ['./file-crime.component.css'],
})
export class FileCrimeComponent implements OnInit {
  crime: any = {
    type: '',
    location: '',
    description: '',
  };

  crimeTypes: string[] = [
    'Cyber Crime',
    'Online Fraud',
    'Theft',
    'Burglary',
    'Shoplifting',
    'Assault',
    'Murder',
    'Kidnapping',
    'Domestic Violence',
    'Robbery',
    'Armed Robbery',
    'Bank Robbery',
    'Drug Trafficking',
    'Possession of Drugs',
    'Sexual Harassment',
    'Rape',
    'Stalking',
    'Vandalism',
    'Traffic Violation',
    'Hit and Run',
  ];

  selectedDate: string = '';
  selectedTime: string = '';
  map: any;
  marker: any;

  constructor(
    private crimeService: CrimeService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.initMap();
  }

  initMap(): void {
    this.map = L.map('map').setView([19.7515, 75.7139], 6); // Maharashtra center

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors',
    }).addTo(this.map);

    const provider = new OpenStreetMapProvider();

    const searchControl = GeoSearchControl({
      provider: provider,
      style: 'bar',
      autoClose: true,
      retainZoomLevel: false,
      searchLabel: 'Enter location...',
      keepResult: true,
    });

    this.map.addControl(searchControl);

    this.map.on('click', async (e: any) => {
      const lat = e.latlng.lat;
      const lng = e.latlng.lng;
      this.setMarker(lat, lng);

      try {
        const res = await fetch(
          `https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json`
        );
        const data = await res.json();

        if (data && data.display_name) {
          this.crime.location = data.display_name;
        } else {
          this.crime.location = `${lat.toFixed(4)}, ${lng.toFixed(4)}`;
        }
      } catch (err) {
        console.error('Reverse geocoding failed:', err);
        this.crime.location = `${lat.toFixed(4)}, ${lng.toFixed(4)}`;
      }
    });
  }

  setMarker(lat: number, lng: number): void {
    if (this.marker) {
      this.marker.setLatLng([lat, lng]);
    } else {
      this.marker = L.marker([lat, lng], {
        icon: L.icon({
          iconUrl:
            'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
          shadowUrl:
            'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
        }),
      }).addTo(this.map);
    }

    this.map.setView([lat, lng], 14);
  }

  onSubmit(): void {
    const userId = Number(localStorage.getItem('userId'));
    if (!userId) {
      alert('User not logged in!');
      return;
    }

    const complaint = {
      ...this.crime,
      date: this.selectedDate,
      time: this.selectedTime,
    };

    this.crimeService.createCrime(userId, complaint).subscribe({
      next: (res) => {
        console.log('✅ Complaint sent to backend:', res);
        alert('Complaint filed successfully!');

        // 🔔 Push new notification to stream (for navbar live update)
        this.notificationService.refreshNotifications(userId);

        // 🧹 Reset form
        this.crime = { type: '', location: '', description: '' };
        this.selectedDate = '';
        this.selectedTime = '';
      },
      error: (err) => {
        console.error('❌ Error sending complaint:', err);
        alert('Failed to send complaint. Please try again.');
      },
    });
  }
}
