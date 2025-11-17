import { Component, OnInit } from '@angular/core';
import { CrimeService } from '../../services/crime.service';
import { OfficerService } from '../../services/officer.service';
import { Crime } from '../../models/crime.model';
import { Officer } from '../../models/officer.model';

@Component({
  selector: 'app-crimes',
  templateUrl: './view-crimes.component.html',
  styleUrls: ['./view-crimes.component.css'],
})
export class CrimesComponent implements OnInit {
  crimes: Crime[] = [];
  officers: Officer[] = [];
  loading: boolean = false;
  isLoadingOfficers: boolean = false;
  errorMessage: string = '';
  officerError: string = '';
  searchText: string = '';
  selectedStatus: string = '';
  sortColumn: keyof Crime = 'date';
  sortAscending: boolean = false;
  userId: number = 0;

  constructor(
    private crimeService: CrimeService,
    private officerService: OfficerService
  ) {}

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.loadCrimes();
    this.loadOfficers();
  }

  loadCrimes(): void {
    this.loading = true;
    this.errorMessage = '';

    const user = JSON.parse(localStorage.getItem('user') || '{}');
    this.userId = user.id || 1;
    const role = user.role || 'CITIZEN';

    this.crimeService.getComplaintsByUser(this.userId, role).subscribe({
      next: (data: any[]) => {
        this.crimes = data.map((c) => {
          const crime: Crime = {
            id: c.id,
            description: c.description,
            type: c.type,
            location: c.location,
            status: c.status,
            date: c.date,
            time: c.time,
            userId: c.userId,
            officerId: c.officerId ? Number(c.officerId) : null,
            officername: c.officername,
            assignedOfficer: c.officername
              ? {
                  id: c.officerId ? Number(c.officerId) : null,
                  name: c.officername,
                }
              : undefined,
            assignedOfficerId: c.officerId ? Number(c.officerId) : undefined,
          };
          return crime;
        });
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading crimes:', err);
        this.errorMessage = 'Failed to load crime data. Please try again.';
        this.loading = false;
      },
    });
  }

  loadOfficers(): void {
    this.isLoadingOfficers = true;
    this.officerError = '';

    this.officerService.getAllOfficers().subscribe({
      next: (data: Officer[]) => {
        this.officers = data;
        this.isLoadingOfficers = false;
      },
      error: (err) => {
        console.error('Error loading officers:', err);
        this.officerError = 'Failed to load officers. Please try again.';
        this.isLoadingOfficers = false;
      },
    });
  }

  assignOfficer(crime: Crime): void {
    if (!crime.assignedOfficerId) {
      this.errorMessage = 'Please select an officer first!';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.crimeService
      .assignOfficer(crime.id, crime.assignedOfficerId)
      .subscribe({
        next: () => {
          // Update the local crime data
          const updatedCrime = this.crimes.find((c) => c.id === crime.id);
          if (updatedCrime) {
            const officer = this.officers.find(
              (o) => o.id === crime.assignedOfficerId
            );
            if (officer) {
              updatedCrime.assignedOfficer = {
                id: officer.id,
                name: officer.name,
              };
              updatedCrime.officerId = officer.id;
              updatedCrime.officername = officer.name;
            }
          }
          this.loading = false;
        },
        error: (err) => {
          console.error('Error assigning officer:', err);
          this.errorMessage = 'Failed to assign officer. Please try again.';
          this.loading = false;
        },
      });
  }

  isAdmin(): boolean {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    return user.role === 'ADMIN';
  }

  formatDateTime(date: string, time: string): string {
    if (!date || !time) return 'N/A';

    try {
      const cleanTime = time.split('.')[0];
      const dateTime = new Date(`${date}T${cleanTime}`);

      if (isNaN(dateTime.getTime())) {
        console.warn('Invalid date/time:', date, time);
        return 'Invalid Date';
      }

      return dateTime.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch (e) {
      console.error('Error formatting date/time:', e);
      return 'Format Error';
    }
  }

  filteredCrimes(): Crime[] {
    let filtered = this.crimes.filter((crime) => {
      const matchesSearch =
        crime.type.toLowerCase().includes(this.searchText.toLowerCase()) ||
        crime.location.toLowerCase().includes(this.searchText.toLowerCase()) ||
        (crime.description &&
          crime.description
            .toLowerCase()
            .includes(this.searchText.toLowerCase()));

      const matchesStatus = this.selectedStatus
        ? crime.status === this.selectedStatus
        : true;

      return matchesSearch && matchesStatus;
    });

    return filtered.sort((a, b) => {
      const valA = a[this.sortColumn];
      const valB = b[this.sortColumn];

      if (typeof valA === 'string' && typeof valB === 'string') {
        if (this.sortColumn === 'date') {
          try {
            const dateA = new Date(`${a.date}T${a.time}`).getTime();
            const dateB = new Date(`${b.date}T${b.time}`).getTime();
            return this.sortAscending ? dateA - dateB : dateB - dateA;
          } catch (e) {
            return 0;
          }
        }
        return this.sortAscending
          ? valA.localeCompare(valB)
          : valB.localeCompare(valA);
      }
      return 0;
    });
  }

  sortBy(column: keyof Crime): void {
    if (this.sortColumn === column) {
      this.sortAscending = !this.sortAscending;
    } else {
      this.sortColumn = column;
      this.sortAscending = true;
    }
  }

  retryLoadOfficers(): void {
    this.loadOfficers();
  }

  clearFilters(): void {
    this.searchText = '';
    this.selectedStatus = '';
  }
  clearError(): void {
    this.errorMessage = '';
  }
}
