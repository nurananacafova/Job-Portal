package com.example.jobportal.controller;

import com.example.jobportal.dto.JobDto;
import com.example.jobportal.service.JobService;
import com.example.jobportal.service.WebScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final WebScrapingService webScrapingService;
    private final JobService jobService;

    @GetMapping("/")
    public ResponseEntity<List<JobDto>> getAllJobs(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(jobService.getAllJobs(pageNumber, size), HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<JobDto>> sortAllJobs(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sortBy", required = false, defaultValue = "postedDate") String sortBy,
            @RequestParam(name = "sortDir", required = false, defaultValue = "DESC") String sortDir
    ) {
        return new ResponseEntity<>(jobService.sortAllJobs(pageNumber, size, sortBy, sortDir), HttpStatus.PARTIAL_CONTENT);
    }

    @PostMapping("/load")
    public ResponseEntity<String> scrapeJobs() throws IOException {
        webScrapingService.scrapeAndSaveJobs();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<JobDto>> filterJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return new ResponseEntity<>(jobService.filterJobs(location, jobType, experienceLevel, industry, tags, pageNumber, size), HttpStatus.OK);
    }
}
