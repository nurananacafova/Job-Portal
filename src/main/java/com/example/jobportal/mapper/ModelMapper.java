package com.example.jobportal.mapper;

import com.example.jobportal.dto.JobDto;
import com.example.jobportal.model.Job;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModelMapper {
    JobDto jobToJobDto(Job job);

    Job jobDtoToJob(JobDto jobDto);

    List<JobDto> jobListToJobDtoList(List<Job> jobList);

    List<Job> jobDtoListToJobList(List<JobDto> jobDtoList);
}
