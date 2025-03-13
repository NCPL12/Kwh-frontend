import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { environment } from './../../environments/environment';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'floor-energy-management',
  standalone: true,
  imports: [FormsModule, CommonModule, HttpClientModule],
  templateUrl: './floor-energy-management.component.html', // Ensure correct path
  styleUrls: ['./floor-energy-management.component.css']
})
export class FloorEnergyManagementComponent implements OnInit {
  floorEnergyMeters: any[] = [];
  currentFloorMeter: any = {};
  showForm = false;
  isEdit = false;
  showErrors = false;
  availableFloors: any[] = [];
  availableEnergyMeters: any[] = [];
  private apiBaseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.fetchFloorEnergyMappings();
  }

  /** Fetch floor-energy mappings and map names */
  fetchFloorEnergyMappings(): void {
    const floorEnergyAPI = `${this.apiBaseUrl}/get-all-floor-energy-meters`;
    const floorsAPI = `${this.apiBaseUrl}/get-all-floors`;
    const energyMetersAPI = `${this.apiBaseUrl}/all-active-energy-meters`;

    forkJoin({
      floorEnergyMappings: this.http.get<any[]>(floorEnergyAPI),
      floors: this.http.get<any[]>(floorsAPI),
      energyMeters: this.http.get<any[]>(energyMetersAPI)
    }).subscribe({
      next: ({ floorEnergyMappings, floors, energyMeters }) => {
        this.floorEnergyMeters = floorEnergyMappings.map(mapping => ({
          ...mapping,
          floorName: floors.find(f => f.id === mapping.floorId)?.floorName || 'Unknown',
          energyMeterName: energyMeters.find(em => em.id === mapping.energyMeterId)?.name || 'Unknown'
        }));
        this.availableFloors = floors;
        this.availableEnergyMeters = energyMeters;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching data:', err)
    });
  }

  /** Opens the form for adding a new mapping */
  onAddFloorEnergyMapping(): void {
    this.currentFloorMeter = {};
    this.isEdit = false;
    this.showForm = true;
    this.showErrors = false;
  }

  /** Opens the form for editing an existing mapping */
  onEditFloorEnergyMapping(id: number): void {
    const mapping = this.floorEnergyMeters.find((m) => m.id === id);
    if (mapping) {
      this.currentFloorMeter = { ...mapping };
      this.isEdit = true;
      this.showForm = true;
      this.showErrors = false;
    }
  }

  /** Validates the form before saving */
  validateForm(): boolean {
    return !!this.currentFloorMeter.floorId && !!this.currentFloorMeter.energyMeterId;
  }

  /** Saves a new or edited floor-energy mapping */
  onSaveFloorEnergyMapping(): void {
    this.showErrors = true;
    if (!this.validateForm()) {
      console.error('Form validation failed', this.currentFloorMeter);
      return;
    }

    const request = this.isEdit
      ? this.http.put(`${this.apiBaseUrl}//update-floor-energy-meter  `, this.currentFloorMeter, { responseType: 'text' })
      : this.http.post(`${this.apiBaseUrl}/add-floor-energy-meter`, this.currentFloorMeter, { responseType: 'text' });

    request.subscribe({
      next: () => {
        this.showForm = false;
        this.fetchFloorEnergyMappings();
      },
      error: (err) => console.error(`Error ${this.isEdit ? 'updating' : 'adding'} mapping:`, err)
    });
  }

  /** Closes the form */
  onCancel(): void {
    this.showForm = false;
  }
}
