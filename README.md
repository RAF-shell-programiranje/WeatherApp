# Weather App - Docker Exercise

A fullstack weather application with a Java/Spring Boot backend and Angular frontend. This project is used as an exercise for learning Docker containerization.

## Application Overview

### Features
- **Dashboard**: Weather statistics, charts, and data tables
- **World Map**: Interactive map with country weather data
- **REST API**: Endpoints for weather data and system monitoring
- **Data Import**: CSV loader for 118,000+ weather records

### Tech Stack

| Layer | Technology | Port |
|-------|------------|------|
| Backend | Java 17, Spring Boot 4.0.1 | 8080 |
| Frontend | Angular 12, Nginx | 80 |
| Database | MySQL 8.0 | 3306 |

## Project Structure

```
WeatherApp/
├── src/main/java/          # Java backend source
├── frontend/               # Angular frontend
├── data/                   # CSV data file
│   └── GlobalWeatherRepository.csv
├── pom.xml                 # Maven configuration
├── Dockerfile              # TODO: Backend Dockerfile
├── docker-compose.yml      # TODO: Docker Compose
└── frontend/
    ├── Dockerfile          # TODO: Frontend Dockerfile
    └── nginx.conf          # Provided: Nginx configuration
```

---

## Running Locally (Without Docker)

### Prerequisites
- Java 17+
- Node.js 14+
- MySQL 8.0+

### 1. Start MySQL and create database

```bash
mysql -u root -p
CREATE DATABASE weatherapp;
```

### 2. Configure database credentials

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Start Backend

```bash
./mvnw spring-boot:run
```

### 4. Start Frontend

```bash
cd frontend
npm install
npm start
```

### 5. Access Application
- Frontend: http://localhost:4200
- Backend API: http://localhost:8080/api

---

## Docker Exercise

### Objective
Containerize this fullstack application using Docker. You will create:
1. Dockerfile for the Spring Boot backend
2. Dockerfile for the Angular frontend
3. Docker Compose file to orchestrate all services

> **Note:** The Nginx configuration (`frontend/nginx.conf`) is already provided.

---

### Task 1: Backend Dockerfile

Create a `Dockerfile` in the project root for the Spring Boot backend.

**Requirements:**
- Use multi-stage build for smaller image size
- Base image for build stage: `eclipse-temurin:17-jdk-alpine`
- Base image for runtime stage: `eclipse-temurin:17-jre-alpine`
- Copy the Maven wrapper and build the application
- Include the `data/` folder in the final image (needed for CSV import)
- Expose port 8080

**Hints:**
- The Maven wrapper files are `mvnw` and `.mvn/` directory
- Build command: `./mvnw package -DskipTests`
- The JAR file will be in `target/*.jar`

**Structure:**
```dockerfile
# Build stage
FROM ??? AS build
WORKDIR /app
# Copy Maven files and source code
# Build the application

# Runtime stage
FROM ???
WORKDIR /app
# Copy JAR and data folder from build stage
# Expose port and set entrypoint
```

---

### Task 2: Frontend Dockerfile

Create a `Dockerfile` in the `frontend/` directory for the Angular application.

**Requirements:**
- Use multi-stage build
- Base image for build stage: `node:14-alpine`
- Base image for runtime stage: `nginx:alpine`
- Install dependencies and build for production
- Copy built files to Nginx html directory
- Include custom Nginx configuration

**Hints:**
- Install dependencies: `npm ci`
- Build command: `npm run build -- --prod`
- Built files will be in `dist/frontend/`
- Nginx serves files from `/usr/share/nginx/html`

---

### Provided: Nginx Configuration

The `frontend/nginx.conf` file is already provided. It handles:
- Serving Angular static files from `/usr/share/nginx/html`
- Angular routing (redirects all routes to `index.html`)
- Proxying `/api` requests to the backend service at `http://backend:8080`
- Gzip compression and static asset caching

You need to copy this file in your frontend Dockerfile:
```dockerfile
COPY nginx.conf /etc/nginx/conf.d/default.conf
```

---

### Task 3: Docker Compose

Create `docker-compose.yml` in the project root.

**Requirements:**
- Define three services: `mysql`, `backend`, `frontend`
- MySQL configuration:
  - Image: `mysql:8.0`
  - Environment variables for root password, database name, user, password
  - Volume for data persistence
  - Health check for readiness
- Backend configuration:
  - Build from project root Dockerfile
  - Environment variables to override database connection
  - Depends on MySQL (with health check condition)
- Frontend configuration:
  - Build from frontend Dockerfile
  - Expose port 80
  - Depends on backend

**Environment Variables for Backend:**
```
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/weatherapp?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=???
SPRING_DATASOURCE_PASSWORD=???
```

**Structure:**
```yaml
version: '3.8'

services:
  mysql:
    image: ???
    environment:
      # Database configuration
    volumes:
      # Data persistence
    healthcheck:
      # Health check configuration

  backend:
    build:
      # Build configuration
    environment:
      # Override Spring properties
    depends_on:
      # Wait for MySQL

  frontend:
    build:
      # Build configuration
    ports:
      # Expose port
    depends_on:
      # Wait for backend

volumes:
  # Define named volumes
```

---

### Task 4: Build and Run

After completing all files, test your configuration:

```bash
# Build and start all services
docker-compose up --build

# Verify services are running
docker-compose ps

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

**Verification:**
- [ ] Frontend accessible at http://localhost
- [ ] Can load weather data via the UI
- [ ] Dashboard displays charts and statistics
- [ ] Map page shows country markers
- [ ] Health check works: http://localhost/api/monitoring/health

---

## API Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/monitoring/health` | Health check |
| GET | `/api/monitoring/status` | System status |
| GET | `/api/statistics/dashboard` | Dashboard data |
| GET | `/api/statistics/countries` | Country statistics |
| POST | `/api/data/load` | Load CSV data |
| GET | `/api/data/status` | Data load status |

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Backend can't connect to MySQL | Check if MySQL health check passes before backend starts |
| Frontend shows "Failed to load" | Verify backend is running and nginx proxies /api correctly |
| CSV data not loading | Ensure `data/` folder is copied in backend Dockerfile |
| Port already in use | Stop local MySQL/services or change ports in docker-compose |

---

## Bonus Challenges

1. **Add a `.dockerignore`** file to exclude unnecessary files from build context
2. **Implement health checks** for backend and frontend services
3. **Add environment-specific configuration** using Docker Compose override files
4. **Set up a production-ready Nginx** configuration with gzip compression and caching
5. **Create a shell script** that waits for MySQL to be ready before starting the backend
