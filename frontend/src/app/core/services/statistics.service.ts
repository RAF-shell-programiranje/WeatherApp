import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ConditionCount, CountryTemperature, DashboardStatistics, WeatherRecord } from '../models/weather.model';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {
  private readonly apiUrl = `${environment.apiUrl}/statistics`;

  constructor(private http: HttpClient) {}

  getDashboardStatistics(): Observable<DashboardStatistics> {
    return this.http.get<DashboardStatistics>(`${this.apiUrl}/dashboard`);
  }

  getConditionDistribution(): Observable<ConditionCount[]> {
    return this.http.get<ConditionCount[]>(`${this.apiUrl}/conditions`);
  }

  getTemperatureByCountry(): Observable<CountryTemperature[]> {
    return this.http.get<CountryTemperature[]>(`${this.apiUrl}/temperature-by-country`);
  }

  getHottestLocations(limit: number = 10): Observable<WeatherRecord[]> {
    return this.http.get<WeatherRecord[]>(`${this.apiUrl}/hottest?limit=${limit}`);
  }

  getColdestLocations(limit: number = 10): Observable<WeatherRecord[]> {
    return this.http.get<WeatherRecord[]>(`${this.apiUrl}/coldest?limit=${limit}`);
  }
}
