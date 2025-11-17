import { Component, OnInit } from '@angular/core';
import { OfficerService } from 'src/app/services/officer.service';

@Component({
  selector: 'app-officers',
  templateUrl: './officers.component.html',
  styleUrls: ['./officers.component.css'],
})
export class OfficersComponent implements OnInit {
  officers: any[] = [];

  constructor(private officerService: OfficerService) {}

  ngOnInit(): void {
    this.officerService.getAllOfficers().subscribe((data) => {
      this.officers = data;
    });
  }
}
