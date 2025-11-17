import { Component, OnInit } from '@angular/core';
import { CrimeService } from './services/crime.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  crimes: any[] = [];

  constructor(private crimeService: CrimeService) {}

  ngOnInit() {
    this.crimeService.getCrimes().subscribe({
      next: (data) => {
        console.log('Crimes fetched successfully', data);
        this.crimes = data;
      },
      error: (error) => {
        console.error('Error fetching crimes:', error);
      },
    });
  }
}
