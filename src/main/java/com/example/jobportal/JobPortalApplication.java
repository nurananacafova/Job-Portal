package com.example.jobportal;

import com.example.jobportal.service.WebScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class JobPortalApplication implements CommandLineRunner {
    private final WebScrapingService webScrapingService;

    public static void main(String[] args) {
        SpringApplication.run(JobPortalApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        webScrapingService.scrapeAndSaveJobs();
    }
}
