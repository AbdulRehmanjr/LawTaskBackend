package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.Job;

public interface JobService {

    Job createJob(Job job);

    Job getByJobId(String jobId);


    List<Job> getJobsByUserId(String userId);

    List<Job> getJobsByJobName(String jobName);

    List<Job> getJobsByCategoryName(String categoryName);

    List<Job> getJobsByCategoryId(int id);

    Job updateJob(Job job);
}
