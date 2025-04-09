package com.example.jobportal.model;

import com.example.jobportal.enums.EducationLevel;
import com.example.jobportal.enums.ExperienceLevel;
import com.example.jobportal.enums.Industry;
import com.example.jobportal.enums.JobType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String location;
//    @Enumerated(EnumType.STRING)
//    private JobType jobType;
    private String jobType;
    private String salaryRange;

    private String jobDescription;
    private String requirements;
//    @Enumerated(EnumType.STRING)
//    private ExperienceLevel experienceLevel;
    private String experienceLevel;
//    @Enumerated(EnumType.STRING)
//    private EducationLevel educationLevel;
    private String educationLevel;
//    private Industry industry;
    private String industry;
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
