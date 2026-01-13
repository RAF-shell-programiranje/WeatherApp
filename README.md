# Weather App

A fullstack weather application with a Java/Spring Boot backend and Angular frontend, displaying global weather data with an interactive dashboard and world map visualization.

## Features

- **Dashboard**: Overview cards, charts showing weather condition distribution and temperature by country, hottest/coldest locations tables
- **World Map**: Interactive Leaflet map with country markers color-coded by temperature
- **REST API**: Endpoints for weather data, statistics, locations, and system monitoring
- **Data Import**: Batch CSV loader for 118,000+ weather records

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 17, Spring Boot 4.0.1, Spring Data JPA |
| Frontend | Angular 12, ng2-charts, Leaflet |
| Database | MySQL 8.0 |
| Build Tools | Maven, npm |

## Project Structure

```
WeatherApp/
├── src/main/java/raf/shell/weatherapp/
│   ├── controller/       # REST controllers
│   ├── service/          # Business logic
│   ├── repository/       # Data access layer
│   ├── entity/           # JPA entities
│   ├── dto/              # Data transfer objects
│   └── config/           # Configuration classes
├── frontend/
│   └── src/app/
│       ├── core/         # Services and models
│       └── features/     # Dashboard and Map modules
├── data/
│   └── GlobalWeatherRepository.csv
└── docker/               # Docker configuration
```

## Prerequisites

- Java 17+
- Node.js 14+
- MySQL 8.0+
- Maven 3.8+

---

## Running Locally

### 1. Database Setup

Start MySQL and create the database (it will be auto-created, but ensure MySQL is running):

```bash
# Start MySQL service
sudo service mysql start

# Or on Windows
net start mysql
```

### 2. Configure Database Credentials

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/weatherapp?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Start the Backend

```bash
cd WeatherApp

# Using Maven wrapper
./mvnw spring-boot:run

# Or with Maven installed
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 4. Start the Frontend

```bash
cd WeatherApp/frontend

# Install dependencies (first time only)
npm install

# Start development server
npm start
```

The frontend will start on `http://localhost:4200`

### 5. Load Weather Data

1. Open `http://localhost:4200` in your browser
2. Click the "Load Weather Data" button
3. Wait for the CSV import to complete (~118,000 records)

---

## API Endpoints

### Weather Data
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/weather` | Get paginated weather records |
| GET | `/api/weather/{id}` | Get weather record by ID |
| GET | `/api/weather/country/{country}` | Get records by country |

### Statistics
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/statistics/dashboard` | Get dashboard statistics |
| GET | `/api/statistics/countries` | Get country statistics for map |
| GET | `/api/statistics/hottest?limit=10` | Get hottest locations |
| GET | `/api/statistics/coldest?limit=10` | Get coldest locations |

### Monitoring
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/monitoring/health` | Health check |
| GET | `/api/monitoring/status` | System status |
| GET | `/api/monitoring/metrics` | JVM and data metrics |

### Data Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/data/load` | Load CSV data |
| GET | `/api/data/status` | Check data load status |

---

## Dockerization

### Docker Files

Create the following files in the project root:

#### Dockerfile (Backend)

```dockerfile
# Backend Dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY data data
RUN chmod +x mvnw && ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/data data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### frontend/Dockerfile (Frontend)

```dockerfile
# Frontend Dockerfile
FROM node:14-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build --prod

FROM nginx:alpine
COPY --from=build /app/dist/frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

#### frontend/nginx.conf

```nginx
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

#### docker-compose.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: weatherapp-mysql
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: weatherapp
      MYSQL_USER: weatherapp
      MYSQL_PASSWORD: weatherapp
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5

  backend:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: weatherapp-backend
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/weatherapp?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: weatherapp
      SPRING_DATASOURCE_PASSWORD: weatherapp
    ports:
      - "8080:8080"
    depends_on:
      mysql:
        condition: service_healthy

  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: weatherapp-frontend
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  mysql_data:
```

### Running with Docker Compose

```bash
# Build and start all services
docker-compose up --build

# Run in background
docker-compose up -d --build

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Accessing the Application

- **Frontend**: http://localhost
- **Backend API**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/api/monitoring/health

### Building Individual Images

```bash
# Build backend image
docker build -t weatherapp-backend .

# Build frontend image
docker build -t weatherapp-frontend ./frontend

# Run backend with external MySQL
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/weatherapp \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  weatherapp-backend
```

---

## Environment Variables

### Backend

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | MySQL connection URL | `jdbc:mysql://localhost:3306/weatherapp` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `root` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | - |
| `CSV_FILE_PATH` | Path to CSV data file | `data/GlobalWeatherRepository.csv` |

### Frontend

| Variable | Description | Default |
|----------|-------------|---------|
| `API_URL` | Backend API URL | `http://localhost:8080/api` |

---

## Development

### Running Tests

```bash
# Backend tests
./mvnw test

# Frontend tests
cd frontend && npm test
```

### Building for Production

```bash
# Backend
./mvnw package -DskipTests

# Frontend
cd frontend && npm run build --prod
```

---

## Troubleshooting

### Common Issues

1. **Database connection failed**
   - Ensure MySQL is running
   - Verify credentials in `application.properties`
   - Check if port 3306 is available

2. **CSV data not loading**
   - Verify `data/GlobalWeatherRepository.csv` exists
   - Check backend logs for parsing errors

3. **Frontend can't connect to backend**
   - Ensure backend is running on port 8080
   - Check CORS configuration
   - Verify `environment.ts` has correct API URL

4. **Docker build fails**
   - Ensure Docker daemon is running
   - Check available disk space
   - Verify all required files exist

---

## License

This project is for educational purposes.
