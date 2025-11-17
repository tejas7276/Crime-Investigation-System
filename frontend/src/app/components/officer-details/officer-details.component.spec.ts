import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfficerDetailsComponent } from './officer-details.component';

describe('OfficerDetailsComponent', () => {
  let component: OfficerDetailsComponent;
  let fixture: ComponentFixture<OfficerDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OfficerDetailsComponent]
    });
    fixture = TestBed.createComponent(OfficerDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
