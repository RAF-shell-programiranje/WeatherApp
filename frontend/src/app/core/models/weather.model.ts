export interface Location {
  id: number;
  country: string;
  locationName: string;
  latitude: number;
  longitude: number;
  timezone: string;
}

export interface AirQuality {
  carbonMonoxide: number;
  ozone: number;
  nitrogenDioxide: number;
  sulphurDioxide: number;
  pm25: number;
  pm10: number;
  usEpaIndex: number;
  gbDefraIndex: number;
}

export interface Astronomy {
  sunrise: string;
  sunset: string;
  moonrise: string;
  moonset: string;
  moonPhase: string;
  moonIllumination: number;
}

export interface WeatherRecord {
  id: number;
  location: Location;
  lastUpdated: string;
  temperatureCelsius: number;
  temperatureFahrenheit: number;
  feelsLikeCelsius: number;
  feelsLikeFahrenheit: number;
  conditionText: string;
  humidity: number;
  cloud: number;
  visibilityKm: number;
  uvIndex: number;
  windMph: number;
  windKph: number;
  windDegree: number;
  windDirection: string;
  gustMph: number;
  gustKph: number;
  pressureMb: number;
  pressureIn: number;
  precipMm: number;
  precipIn: number;
  airQuality: AirQuality;
  astronomy: Astronomy;
}

export interface ConditionCount {
  condition: string;
  count: number;
}

export interface CountryTemperature {
  country: string;
  averageTemperature: number;
}

export interface DashboardStatistics {
  totalRecords: number;
  totalLocations: number;
  totalCountries: number;
  averageTemperature: number;
  averageHumidity: number;
  averagePressure: number;
  conditionDistribution: ConditionCount[];
  temperatureByCountry: CountryTemperature[];
  hottestLocations: WeatherRecord[];
  coldestLocations: WeatherRecord[];
}

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
