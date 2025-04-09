package com.example.jobportal.controller;

import com.example.jobportal.JobRepository;
import com.example.jobportal.dto.JobDto;
import com.example.jobportal.model.Job;
import com.example.jobportal.service.JobService;
import com.example.jobportal.service.WebScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final WebScrapingService webScrapingService;
    private final JobService jobService;

    @GetMapping("/web")
    public List<JobDto> webScrapeJobTitles() throws IOException {
        return webScrapingService.scrapeJobs();
    }

    @GetMapping("/")
    public ResponseEntity<List<Job>> getAllJobs() {
        return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
    }
}
