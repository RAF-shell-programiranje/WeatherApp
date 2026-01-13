import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PagedResponse, WeatherRecord } from '../models/weather.model';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  private readonly apiUrl = `${environment.apiUrl}/weather`;

  constructor(private http: HttpClient) {}

  getWeatherRecords(page: number = 0, size: number = 20): Observable<PagedResponse<WeatherRecord>> {
    return this.http.get<PagedResponse<WeatherRecord>>(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  getWeatherById(id: number): Observable<WeatherRecord> {
    return this.http.get<WeatherRecord>(`${this.apiUrl}/${id}`);
  }

  getWeatherByCountry(country: string): Observable<WeatherRecord[]> {
    return this.http.get<WeatherRecord[]>(`${this.apiUrl}/country/${country}`);
  }

  getWeatherByCondition(condition: string): Observable<WeatherRecord[]> {
    return this.http.get<WeatherRecord[]>(`${this.apiUrl}/condition/${condition}`);
  }

  getWeatherByTemperatureRange(min: number, max: number): Observable<WeatherRecord[]> {
    return this.http.get<WeatherRecord[]>(`${this.apiUrl}/temperature?min=${min}&max=${max}`);
  }
}
