import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-monthly-report',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './monthly-report.component.html',
  styleUrls: ['./monthly-report.component.css']
})
export class MonthlyReportComponent {
  selectedMonthYear: string = '';
  showErrors: boolean = false;
  private apiBaseUrl = 'http://localhost:8080/bms-reports';

  constructor(private http: HttpClient) {
    this.setDefaultMonthYear();
  }

  setDefaultMonthYear(): void {
    const today = new Date();
    today.setMonth(today.getMonth() - 1); // Set to the previous month
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0'); // Ensure two digits
    this.selectedMonthYear = `${year}-${month}`;
  }

  onInputChange(): void {
    this.showErrors = !this.selectedMonthYear;
  }

  exportPdfReport(): void {
    if (!this.selectedMonthYear) {
      this.showErrors = true;
      return;
    }

    const [year, month] = this.selectedMonthYear.split('-');
    const endpoint = `${this.apiBaseUrl}/v1/monthly-report/generate-bill`;

    const queryParams = `?month=${month}&year=${year}`;
    window.open(endpoint + queryParams, '_blank');
  }
}
