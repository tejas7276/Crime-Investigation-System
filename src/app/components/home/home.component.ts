import { Component, OnInit } from '@angular/core';
import { CrimeService } from '../../services/crime.service';
import { Crime } from '../../models/crime.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  totalCrimes: number = 0;
  resolvedCrimes: number = 0;
  pendingCrimes: number = 0;

  constructor(private crimeService: CrimeService) {}

  ngOnInit(): void {
    this.fetchCrimeStats();
  }

  fetchCrimeStats(): void {
    this.crimeService.getCrimes().subscribe((crimes: Crime[]) => {
      this.totalCrimes = crimes.length;
      this.resolvedCrimes = crimes.filter(
        (c: Crime) => c.status === 'RESOLVED'
      ).length;
      this.pendingCrimes = crimes.filter(
        (c: Crime) => c.status === 'PENDING'
      ).length;
    });
  }
}
