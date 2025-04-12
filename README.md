# Job Listing API

This is a backend system for a job portal designed to manage and expose job listings with structured fields that allow
for efficient filtering, searching, and retrieval. The system scraping data from external sources such as Djinni.co.

---

## 🚀 Features

- ✅ Create, retrieve, and manage job listings
- 🔍 Filter jobs by location, type, industry, and more
- 🕵️‍♂️ Scrape jobs from Djinni.co
- 🗃️ Load job listings into a database

[//]: # (- 🐳 Fully dockerized infrastructure)

- 📄 Swagger documentation

---

## 🛠️ Technologies Used

- Java + Spring Boot
- PostgreSQL
- Docker
- Swagger for API docs
- JSoup (for web scraping)

---

## 🔌 API Endpoints

### Job Endpoints

- `GET http://localhost:8080/jobs/?pagenNumver=1&size=10` – Get all jobs with pagination.
- `GET http://localhost:8080/jobs/filter?pagenNumber=1&size=10&location=Worldwide&jobType=Full Remote&experienceLevel=2&industry=Tech&tags=linux` – Filter jobs with given criterias.
- `GET http://localhost:8080/jobs/sort?pagenNumber=1&size=10&sortBy=postedDate&sortDir=ASC` – Get Sorted jobs with pagination.
#### Note!
1. sortBy work only for 'postedDate' and 'salaryRange'
2. Filter criterias:
- Location
- Job Type(ex. Full Remote)
- Experience level (year)
- Tags (one or more keyword for filter)
- Industry (Tech, Finance and etc)

### How Scraper works

It retrieve data from Djinny based on these criterias:

- Remote + Worldwide
- Remote + Azerbaijan
- Offer relocation

---

## Project Setup

### Prerequisites

- Java 17+
- Docker & Docker Compose

### 1. Clone the Repository

```
git https://github.com/nurananacafova/Job-Portal.git
```

### 2. Go to root directory and run docker-compose.yaml

```
docker-compose.up
```
### 3. Go to : http://localhost:8080/jobs/swagger-ui/index.html
