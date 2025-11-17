import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CrimeService } from 'src/app/services/crime.service';

@Component({
  selector: 'app-crime-details',
  templateUrl: './crime-details.component.html',
  styleUrls: ['./crime-details.component.css'],
})
export class CrimeDetailsComponent implements OnInit {
  crime: any;

  constructor(
    private route: ActivatedRoute,
    private crimeService: CrimeService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.crimeService.getCrimeById(+id).subscribe((data) => {
        this.crime = data;
      });
    }
  }
}
