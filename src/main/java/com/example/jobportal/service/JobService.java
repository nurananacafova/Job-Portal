package com.example.jobportal.service;

import com.example.jobportal.dto.JobDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {
    List<JobDto> getAllJobs(int pageNumber, int size);

    List<JobDto> sortAllJobs(int pageNumber, int size, String sortBy, String sortDir);

    List<JobDto> filterJobs(String location,
                            String jobType,
                            String experienceLevel,
                            String industry,
                            List<String> tags,
                            int pageNumber,
                            int size);

}
