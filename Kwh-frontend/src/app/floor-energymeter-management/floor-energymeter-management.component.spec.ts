import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FloorEnergymeterManagementComponent } from './floor-energymeter-management.component';

describe('FloorEnergymeterManagementComponent', () => {
  let component: FloorEnergymeterManagementComponent;
  let fixture: ComponentFixture<FloorEnergymeterManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FloorEnergymeterManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FloorEnergymeterManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
