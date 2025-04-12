-- Create database
-- CREATE DATABASE jobs;

-- Create table
CREATE TABLE job
(
    id                   SERIAL PRIMARY KEY,
    job_title            VARCHAR(255),
    company_name         VARCHAR(255),
    location             VARCHAR(255),
    job_type             VARCHAR(255),
    salary_range         VARCHAR(255),
    job_description      TEXT,
    requirements         TEXT,
    experience_level     VARCHAR(255),
    education_level      VARCHAR(255),
    industry             VARCHAR(255),
    posted_date          VARCHAR(255),
    application_deadline VARCHAR(255),
    how_to_apply         VARCHAR(255),
    company_logo         VARCHAR(255),
    benefits             VARCHAR(255),
    tags                 TEXT[],
    source               VARCHAR(255)
);
