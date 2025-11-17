import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { OfficerService } from 'src/app/services/officer.service';

@Component({
  selector: 'app-officer-details',
  templateUrl: './officer-details.component.html',
  styleUrls: ['./officer-details.component.css'],
})
export class OfficerDetailsComponent implements OnInit {
  officer: any;

  constructor(
    private route: ActivatedRoute,
    private officerService: OfficerService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.officerService.getOfficerById(+id).subscribe((data) => {
        this.officer = data;
      });
    }
  }
}
