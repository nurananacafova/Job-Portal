package com.example.jobportal.service.impl;

import com.example.jobportal.dto.JobDto;
import com.example.jobportal.mapper.ModelMapper;
import com.example.jobportal.model.Job;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.service.JobService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {
    private final EntityManager entityManager;

    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<JobDto> getAllJobs(int pageNumber, int size) {
        Pageable pageRequestData = PageRequest.of(pageNumber - 1, size);
        Page<Job> jobs = jobRepository.findAll(pageRequestData);
        log.info("Total job size: {}", jobs.getContent().size());
        return modelMapper.jobListToJobDtoList(jobs.getContent());
    }

    @Override
    public List<JobDto> sortAllJobs(int pageNumber, int size, String sortBy, String sortDir) {
        if (!sortBy.equalsIgnoreCase("postedDate") && !sortBy.equalsIgnoreCase("salaryRange")) {
            throw new IllegalArgumentException("You can only sort by 'postedDate' or 'salaryRange'");
        }

        Pageable pageRequestData = PageRequest.of(pageNumber - 1, size, Sort.Direction.valueOf(sortDir), sortBy);
        Page<Job> sortedJobs = jobRepository.findAll(pageRequestData);
        log.info("Sorted job size: {}", sortedJobs.getContent().size());
        return modelMapper.jobListToJobDtoList(sortedJobs.getContent());
    }

    @Override
    public List<JobDto> filterJobs(String location,
                                   String jobType,
                                   String experienceLevel,
                                   String industry,
                                   List<String> tags,
                                   int pageNumber,
                                   int size) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Job> query = cb.createQuery(Job.class);
        Root<Job> root = query.from(Job.class);
        List<Predicate> predicates = new ArrayList<>();

        if (location != null && !location.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
        }

        if (jobType != null && !jobType.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("jobType")), "%" + jobType.toLowerCase() + "%"));
        }

        if (experienceLevel != null && !experienceLevel.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("experienceLevel")), "%" + experienceLevel.toLowerCase() + "%"));
        }

        if (industry != null && !industry.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("industry")), "%" + industry.toLowerCase() + "%"));
        }

        if (tags != null && !tags.isEmpty()) {
            Join<Job, String> tagJoin = root.join("tags", JoinType.LEFT);
            List<Predicate> tagPredicates = tags.stream()
                    .map(tag -> cb.like(cb.lower(tagJoin), "%" + tag.toLowerCase() + "%"))
                    .collect(Collectors.toList());
            predicates.add(cb.or(tagPredicates.toArray(new Predicate[0])));
            query.distinct(true);
        }

//        query.where(cb.and(predicates.toArray(new Predicate[0])));
        if (!predicates.isEmpty()) {
            query.where(cb.or(predicates.toArray(new Predicate[0])));
        }
        query.orderBy(cb.desc(root.get("postedDate")));

        query.orderBy(cb.desc(root.get("postedDate"))); // Optional sorting

        List<Job> resultList = entityManager.createQuery(query)
                .setFirstResult((pageNumber - 1) * size)
                .setMaxResults(size)
                .getResultList();
        log.info("Filtered job size: {}", resultList.size());
        return modelMapper.jobListToJobDtoList(resultList);
    }

}
