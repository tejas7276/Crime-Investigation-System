import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FileCrimeComponent } from './file-crime.component';

describe('FileCrimeComponent', () => {
  let component: FileCrimeComponent;
  let fixture: ComponentFixture<FileCrimeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FileCrimeComponent]
    });
    fixture = TestBed.createComponent(FileCrimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
