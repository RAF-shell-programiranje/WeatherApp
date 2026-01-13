import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../core/services/statistics.service';
import { DataLoaderService, DataLoadStatus } from '../../core/services/data-loader.service';
import { DashboardStatistics } from '../../core/models/weather.model';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import { Label, SingleDataSet, Color } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  statistics: DashboardStatistics | null = null;
  loading = true;
  error: string | null = null;
  dataStatus: DataLoadStatus | null = null;
  loadingData = false;

  // Pie chart for weather conditions
  public pieChartLabels: Label[] = [];
  public pieChartData: SingleDataSet = [];
  public pieChartType: ChartType = 'pie';
  public pieChartColors: Color[] = [{
    backgroundColor: [
      '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF',
      '#FF9F40', '#FF6384', '#C9CBCF', '#7BC225', '#F7464A'
    ]
  }];
  public pieChartOptions: ChartOptions = {
    responsive: true,
    legend: {
      position: 'right'
    },
    title: {
      display: true,
      text: 'Weather Condition Distribution'
    }
  };

  // Bar chart for temperature by country
  public barChartLabels: Label[] = [];
  public barChartData: ChartDataSets[] = [{
    data: [],
    label: 'Average Temperature (°C)'
  }];
  public barChartType: ChartType = 'bar';
  public barChartOptions: ChartOptions = {
    responsive: true,
    legend: {
      display: false
    },
    title: {
      display: true,
      text: 'Average Temperature by Country (Top 15)'
    },
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: false
        },
        scaleLabel: {
          display: true,
          labelString: 'Temperature (°C)'
        }
      }]
    }
  };
  public barChartColors: Color[] = [{
    backgroundColor: '#36A2EB',
    borderColor: '#2980B9',
    borderWidth: 1
  }];

  constructor(
    private statisticsService: StatisticsService,
    private dataLoaderService: DataLoaderService
  ) {}

  ngOnInit(): void {
    this.checkDataStatus();
  }

  checkDataStatus(): void {
    this.dataLoaderService.getDataLoadStatus().subscribe({
      next: (status) => {
        this.dataStatus = status;
        if (status.dataLoaded) {
          this.loadDashboardData();
        } else {
          this.loading = false;
        }
      },
      error: (err) => {
        this.error = 'Failed to check data status. Is the backend running?';
        this.loading = false;
      }
    });
  }

  loadData(): void {
    this.loadingData = true;
    this.dataLoaderService.loadData().subscribe({
      next: () => {
        this.loadingData = false;
        this.checkDataStatus();
      },
      error: (err) => {
        this.loadingData = false;
        this.error = 'Failed to load data from CSV';
      }
    });
  }

  loadDashboardData(): void {
    this.loading = true;
    this.statisticsService.getDashboardStatistics().subscribe({
      next: (stats) => {
        this.statistics = stats;
        this.updateCharts(stats);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load dashboard statistics';
        this.loading = false;
      }
    });
  }

  updateCharts(stats: DashboardStatistics): void {
    // Update pie chart
    const topConditions = stats.conditionDistribution.slice(0, 10);
    this.pieChartLabels = topConditions.map(c => c.condition);
    this.pieChartData = topConditions.map(c => c.count);

    // Update bar chart
    const topCountries = stats.temperatureByCountry.slice(0, 15);
    this.barChartLabels = topCountries.map(c => c.country);
    this.barChartData = [{
      data: topCountries.map(c => c.averageTemperature),
      label: 'Average Temperature (°C)',
      backgroundColor: topCountries.map(c =>
        c.averageTemperature > 25 ? '#FF6384' :
        c.averageTemperature > 15 ? '#FFCE56' : '#36A2EB'
      )
    }];
  }

  getTemperatureClass(temp: number): string {
    if (temp >= 30) return 'hot';
    if (temp >= 20) return 'warm';
    if (temp >= 10) return 'mild';
    return 'cold';
  }
}
