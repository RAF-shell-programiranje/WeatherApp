import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import * as L from 'leaflet';
import { StatisticsService } from '../../core/services/statistics.service';
import { CountryStatistics } from '../../core/models/country-statistics.model';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit, AfterViewInit, OnDestroy {
  private map!: L.Map;
  private markersLayer!: L.LayerGroup;

  countries: CountryStatistics[] = [];
  loading = true;
  error: string | null = null;
  selectedCountry: CountryStatistics | null = null;

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.loadCountryData();
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }

  private initMap(): void {
    // Fix for default marker icons in Leaflet with webpack
    const iconRetinaUrl = 'assets/marker-icon-2x.png';
    const iconUrl = 'assets/marker-icon.png';
    const shadowUrl = 'assets/marker-shadow.png';

    // Use default marker with custom settings
    const defaultIcon = L.icon({
      iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
      iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });
    L.Marker.prototype.options.icon = defaultIcon;

    this.map = L.map('map', {
      center: [20, 0],
      zoom: 2,
      minZoom: 2,
      maxZoom: 10
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(this.map);

    this.markersLayer = L.layerGroup().addTo(this.map);
  }

  private loadCountryData(): void {
    this.statisticsService.getCountryStatistics().subscribe({
      next: (data) => {
        this.countries = data;
        this.loading = false;
        this.addMarkers();
      },
      error: (err) => {
        this.error = 'Failed to load country data. Is the backend running?';
        this.loading = false;
      }
    });
  }

  private addMarkers(): void {
    if (!this.map || !this.markersLayer) return;

    this.markersLayer.clearLayers();

    this.countries.forEach(country => {
      if (country.latitude && country.longitude) {
        const color = this.getTemperatureColor(country.averageTemperature);

        const circleMarker = L.circleMarker([country.latitude, country.longitude], {
          radius: Math.min(Math.max(Math.sqrt(country.recordCount) / 5, 5), 30),
          fillColor: color,
          color: '#fff',
          weight: 2,
          opacity: 1,
          fillOpacity: 0.7
        });

        const popupContent = `
          <div class="country-popup">
            <h3>${country.country}</h3>
            <p><strong>Avg Temperature:</strong> ${country.averageTemperature?.toFixed(1) || 'N/A'}°C</p>
            <p><strong>Avg Humidity:</strong> ${country.averageHumidity?.toFixed(0) || 'N/A'}%</p>
            <p><strong>Weather Records:</strong> ${country.recordCount?.toLocaleString() || 0}</p>
            <p><strong>Locations:</strong> ${country.locationCount?.toLocaleString() || 0}</p>
          </div>
        `;

        circleMarker.bindPopup(popupContent);
        circleMarker.on('click', () => {
          this.selectedCountry = country;
        });

        this.markersLayer.addLayer(circleMarker);
      }
    });
  }

  private getTemperatureColor(temp: number | null): string {
    if (temp === null || temp === undefined) return '#808080';
    if (temp >= 30) return '#FF0000';
    if (temp >= 25) return '#FF4500';
    if (temp >= 20) return '#FFA500';
    if (temp >= 15) return '#FFD700';
    if (temp >= 10) return '#ADFF2F';
    if (temp >= 5) return '#00CED1';
    if (temp >= 0) return '#1E90FF';
    return '#0000FF';
  }

  selectCountry(country: CountryStatistics): void {
    this.selectedCountry = country;
    if (country.latitude && country.longitude) {
      this.map.setView([country.latitude, country.longitude], 5);
    }
  }

  clearSelection(): void {
    this.selectedCountry = null;
  }

  getTemperatureClass(temp: number): string {
    if (temp >= 30) return 'hot';
    if (temp >= 20) return 'warm';
    if (temp >= 10) return 'mild';
    return 'cold';
  }
}
