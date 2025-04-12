package com.example.jobportal.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface WebScrapingService {
    void scrapeAndSaveJobs() throws IOException;
}
