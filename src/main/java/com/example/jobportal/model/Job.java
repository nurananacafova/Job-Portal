package com.example.jobportal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobTitle;
    private String companyName;
    private String location;
    private String jobType;
    private String salaryRange;
    @Column(length = 2048)
    private String jobDescription;
    @Column(length = 1024)
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
