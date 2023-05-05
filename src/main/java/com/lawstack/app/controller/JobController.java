package com.lawstack.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawstack.app.model.Job;
import com.lawstack.app.service.JobService;


import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {
    
    @Autowired
    private JobService jobService;

    @PostMapping("/save")
    ResponseEntity<?> createJob(@RequestParam("image") MultipartFile jobImage, String job){

        log.info("Making new Job creation request");
        
        Job model = new Job();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            model = objectMapper.readValue(job, Job.class);
        } catch (JsonProcessingException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }

        try {
            model.setJobImage(jobImage.getBytes());
        } catch (IOException e) {
            log.error("Error cause: {}, Message: {}", e.getCause(), e.getMessage());
            return null;
        }

        model = this.jobService.createJob(model);
        if (model != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(model);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);

    }

    @GetMapping("/{userId}")
    ResponseEntity<?> getAllJobsByUserId(@PathVariable String userId){

        log.info("Geting all Jobs by userId");
        List<Job>  jobs = this.jobService.getJobsByUserId(userId);

        if(jobs.isEmpty()==true){
            log.info("jobs is empty ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(jobs);
    }

    @GetMapping("/search")
    ResponseEntity<?> getAllJobsByJobName(@RequestParam String jobName){

        log.info("Geting all jobs by job Name {}",jobName);

        List<Job> jobs = this.jobService.getJobsByJobName(jobName);

        
        if(jobs == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(jobs);
    }

}
