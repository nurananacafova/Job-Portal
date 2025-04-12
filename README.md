# Job Listing API

A backend system for a **Job Portal**, designed to manage and expose job listings. It includes structured fields for
efficient filtering, searching, and retrieval.

Data is scraped from [Djinni.co](https://djinni.co/).

---

## Table of Contents

1. [Features](#features)
2. [Scraper Details](#scraper-details)
3. [Project Setup](#project-setup)
4. [Endpoints](#endpoints)
5. [Built With](#built-with)

---

## Features

- ✅ Create, retrieve, and manage job listings
- 🔍 Filter jobs by location, type, experience, tags/keywords and industry
- 🕵️‍♂️ Web scraper for Djinni.co
- 🗃️ Stores scraped jobs into PostgreSQL database
- 📄 Swagger documentation
- 🐳 Dockerized infrastructure

---

## Scraper Details

When project runs, scraper works automatically and scrape data from [Djinni.co](https://djinni.co/). Add jobs to the
database.
The scraper pulls jobs based on:

- Remote + Worldwide
- Remote + includes Azerbaijan
- Offer relocation

---

## Project Setup

### Prerequisites

- Java 17+
- Docker & Docker Compose

### Setup Steps

#### 1. Clone the Repository

```
git clone https://github.com/nurananacafova/Job-Portal.git
````

#### 2. Go to root folder and run docker-compose.yaml file

```
cd Job-Portal
docker-compose up
```

#### 3. After run docker-compose.yaml, go to: 
http://localhost:8080/jobs/swagger-ui/index.html

#### 4. Test with Swagger

---

## Endpoints

### Get jobs with pagination:

```
GET http://localhost:8080/jobs/?pageNumber={pageNumber}&size={size}
```

### Get jobs with filter:

```
GET http://localhost:8080/jobs/filter?pageNumber={pageNumber}&size={size}&location={location}&jobType={jobType}&experienceLevel={years}&industry={industry}&tags={tags}
```

**Filter Criteria:**

- `location`
- `jobType` (e.g., Full Remote)
- `experienceLevel` (years)
- `tags` (comma-separated keywords)
- `industry` (e.g., Tech, Finance)

### Sort jobs

``` 
GET http://localhost:8080/jobs/sort?pageNumber={pageNumber}&size={size}&sortBy={field}&sortDir={direction}
```

**Sort Fields:**

- `postedDate`
- `salaryRange`

---

## ️ Built With

- Java 17 + Spring Boot
- PostgreSQL
- Docker & Docker Compose
- Swagger (OpenAPI)
- JSoup (Web scraping)
