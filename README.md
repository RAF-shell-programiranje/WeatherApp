# Weather App - Docker vežba

Fullstack aplikacija za vremensku prognozu sa Java/Spring Boot backend-om i Angular frontend-om. Ovaj projekat se koristi kao vežba za učenje Docker kontejnerizacije.

## Pregled aplikacije

### Funkcionalnosti

* **Dashboard**: Statistika vremena, grafikoni i tabele sa podacima.
* **Mapa sveta**: Interaktivna mapa sa podacima o vremenu po državama.
* **REST API**: Endpoint-ovi za vremenske podatke i monitoring sistema.
* **Uvoz podataka**: CSV loader za više od 118,000 zapisa o vremenu.

### Tehnološki stek

| Sloj | Tehnologija | Port |
| --- | --- | --- |
| Backend | Java 17, Spring Boot 4.0.1 | 8080 |
| Frontend | Angular 12, Nginx | 80 |
| Baza podataka | MySQL 8.0 | 3306 |

## Struktura projekta

```
WeatherApp/
├── src/main/java/          # Izvorni kod za Java backend
├── frontend/               # Angular frontend
├── data/                   # CSV datoteka sa podacima
│   └── GlobalWeatherRepository.csv
├── pom.xml                 # Maven konfiguracija
├── Dockerfile              # TODO: Backend Dockerfile
├── docker-compose.yml      # TODO: Docker Compose
└── frontend/
    ├── Dockerfile          # TODO: Frontend Dockerfile
    └── nginx.conf          # Obezbeđeno: Nginx konfiguracija

```

---

## Lokalno pokretanje (bez Docker-a)

### Preduslovi

* Java 17+
* Node.js 14+
* MySQL 8.0+

### 1. Pokrenite MySQL i kreirajte bazu

```bash
mysql -u root -p
CREATE DATABASE weatherapp;

```

### 2. Konfigurišite pristupne podatke za bazu

Izmenite `src/main/resources/application.properties`:

```properties
spring.datasource.username=vas_username
spring.datasource.password=vasa_lozinka

```

### 3. Pokrenite Backend

```bash
./mvnw spring-boot:run

```

### 4. Pokrenite Frontend

```bash
cd frontend
npm install
npm start

```

### 5. Pristup aplikaciji

* Frontend: http://localhost:4200
* Backend API: http://localhost:8080/api

---

## Docker vežba

### Cilj

Kontejnerizujte ovu fullstack aplikaciju koristeći Docker. Kreiraćete:

1. Dockerfile za Spring Boot backend
2. Dockerfile za Angular frontend
3. Docker Compose datoteku za orkestraciju svih servisa

> **Napomena:** Nginx konfiguracija (`frontend/nginx.conf`) je već obezbeđena.

---

### Zadatak 1: Backend Dockerfile

Kreirajte `Dockerfile` u korenu projekta za Spring Boot backend.

**Zahtevi:**

* Koristite "multi-stage build" za manju veličinu slike (image)
* Bazna slika za build fazu: `eclipse-temurin:17-jdk-alpine`
* Bazna slika za runtime fazu: `eclipse-temurin:17-jre-alpine`
* Kopirajte Maven wrapper i bildujte aplikaciju
* Uključite `data/` folder u finalnu sliku (potreban za CSV uvoz)
* Eksponujte port 8080

**Saveti:**

* Datoteke Maven wrapper-a su `mvnw` i `.mvn/` direktorijum
* Komanda za build: `./mvnw package -DskipTests`
* JAR fajl će se nalaziti u `target/*.jar`

**Struktura:**

```dockerfile
# Build faza
FROM ??? AS build
WORKDIR /app
# Kopiranje Maven fajlova i izvornog koda
# Build-ovanje aplikacije

# Runtime faza
FROM ???
WORKDIR /app
# Kopiranje JAR-a i data foldera iz build faze
# Eksponovanje porta i postavljanje entrypoint-a

```

---

### Zadatak 2: Frontend Dockerfile

Kreirajte `Dockerfile` u `frontend/` direktorijumu za Angular aplikaciju.

**Zahtevi:**

* Koristite "multi-stage build"
* Bazna slika za build fazu: `node:14-alpine`
* Bazna slika za runtime fazu: `nginx:alpine`
* Instalirajte zavisnosti (dependencies) i bildujte za produkciju
* Kopirajte bildovane fajlove u Nginx html direktorijum
* Uključite prilagođenu Nginx konfiguraciju

**Saveti:**

* Instalacija zavisnosti: `npm ci`
* Komanda za build: `npm run build -- --prod`
* Bildovani fajlovi će biti u `dist/frontend/`
* Nginx servira fajlove iz `/usr/share/nginx/html`

---

### Obezbeđeno: Nginx konfiguracija

Fajl `frontend/nginx.conf` je već obezbeđen. On upravlja:

* Serviranjem Angular statičkih fajlova iz `/usr/share/nginx/html`
* Angular rutingom (preusmerava sve rute na `index.html`)
* Proksiranjem `/api` zahteva ka backend servisu na `http://backend:8080`
* Gzip kompresijom i keširanjem statičkih resursa

Potrebno je da kopirate ovaj fajl u vaš frontend Dockerfile:

```dockerfile
COPY nginx.conf /etc/nginx/conf.d/default.conf

```

---

### Zadatak 3: Docker Compose

Kreirajte `docker-compose.yml` u korenu projekta.

**Zahtevi:**

* Definišite tri servisa: `mysql`, `backend`, `frontend`
* MySQL konfiguracija:
* Image: `mysql:8.0`
* Environment varijable za root lozinku, ime baze, korisnika i lozinku
* Volume za perzistenciju podataka
* Health check za proveru spremnosti


* Backend konfiguracija:
* Build iz Dockerfile-a u korenu projekta
* Environment varijable za promenu konekcije ka bazi
* Zavisi od MySQL-a (uz health check uslov)


* Frontend konfiguracija:
* Build iz frontend Dockerfile-a
* Eksponujte port 80
* Zavisi od backend-a



**Environment varijable za Backend:**

```
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/weatherapp?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=???
SPRING_DATASOURCE_PASSWORD=???

```

**Struktura:**

```yaml
version: '3.8'

services:
  mysql:
    image: ???
    environment:
      # Konfiguracija baze podataka
    volumes:
      # Perzistencija podataka
    healthcheck:
      # Konfiguracija health check-a

  backend:
    build:
      # Build konfiguracija
    environment:
      # Nadjačavanje Spring propertija
    depends_on:
      # Čekanje na MySQL

  frontend:
    build:
      # Build konfiguracija
    ports:
      # Eksponovanje porta
    depends_on:
      # Čekanje na backend

volumes:
  # Definisanje imenovanih volumena

```

---

### Zadatak 4: Build i pokretanje

Nakon što popunite sve fajlove, testirajte vašu konfiguraciju:

```bash
# Bildovanje i pokretanje svih servisa
docker-compose up --build

# Provera da li servisi rade
docker-compose ps

# Pregled logova
docker-compose logs -f

# Zaustavljanje servisa
docker-compose down

```

**Verifikacija:**

* [ ] Frontend je dostupan na http://localhost
* [ ] Podaci o vremenu se učitavaju kroz korisnički interfejs
* [ ] Dashboard prikazuje grafikone i statistiku
* [ ] Stranica sa mapom prikazuje markere država
* [ ] Health check radi: http://localhost/api/monitoring/health

---

## API Referenca

| Metoda | Endpoint | Opis |
| --- | --- | --- |
| GET | `/api/monitoring/health` | Provera zdravlja sistema (Health check) |
| GET | `/api/monitoring/status` | Status sistema |
| GET | `/api/statistics/dashboard` | Podaci za Dashboard |
| GET | `/api/statistics/countries` | Statistika po državama |
| POST | `/api/data/load` | Učitavanje CSV podataka |
| GET | `/api/data/status` | Status učitavanja podataka |

---

## Rešavanje problema (Troubleshooting)

| Problem | Rešenje |
| --- | --- |
| Backend ne može da se poveže na MySQL | Proverite da li MySQL health check prolazi pre nego što backend krene sa radom |
| Frontend prikazuje "Failed to load" | Proverite da li backend radi i da li nginx ispravno proksira /api |
| CSV podaci se ne učitavaju | Osigurajte da je `data/` folder kopiran u backend Dockerfile |
| Port je već zauzet | Zaustavite lokalni MySQL/servise ili promenite portove u docker-compose fajlu |

---

Želite li da vam pomognem sa pisanjem rešenja za neki od ovih Dockerfile-ova?
