package com.example.jobportal.dto;

import com.example.jobportal.enums.EducationLevel;
import com.example.jobportal.enums.ExperienceLevel;
import com.example.jobportal.enums.Industry;
import com.example.jobportal.enums.JobType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JobDto {
    private String jobTitle;
    private String companyName;
    private String location;
    private String jobType;
    private String salaryRange;
    private String jobDescription;

    private String requirements;

    private String experienceLevel;
    private String educationLevel;
    private Industry industry;
    private LocalDate postedDate;
    private LocalDate applicationDeadline;
    private String howToApply;
    private String companyLogo;
    @ElementCollection
    private List<String> benefits;
    @ElementCollection
    private List<String> tags;
    private String source;
}
