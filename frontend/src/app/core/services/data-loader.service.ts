import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface DataLoadStatus {
  totalRecords: number;
  dataLoaded: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class DataLoaderService {
  private readonly apiUrl = `${environment.apiUrl}/data`;

  constructor(private http: HttpClient) {}

  loadData(): Observable<{ status: string; message: string }> {
    return this.http.post<{ status: string; message: string }>(`${this.apiUrl}/load`, {});
  }

  getDataLoadStatus(): Observable<DataLoadStatus> {
    return this.http.get<DataLoadStatus>(`${this.apiUrl}/status`);
  }
}
