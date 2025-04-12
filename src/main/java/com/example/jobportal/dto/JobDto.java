package com.example.jobportal.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JobDto {
    private Long id;

    private String jobTitle;
    private String companyName;
    private String location;
    private String jobType;
    private String salaryRange;
    private String jobDescription;
    private String requirements;
    private String experienceLevel;
    private String educationLevel;
    private String industry;
    private LocalDate postedDate;
    private LocalDate applicationDeadline;
    private String howToApply;
    private String companyLogo;
    private String benefits;
    @ElementCollection
    private List<String> tags;
    private String source;
}
