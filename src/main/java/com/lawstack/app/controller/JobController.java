package com.lawstack.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            log.error("Error : Job creation error.");
            return ResponseEntity.status(HttpStatus.CREATED).body(model);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);

    }

    @GetMapping("/{userId}")
    ResponseEntity<?> getAllJobsByUserId(@PathVariable String userId){

        
        List<Job>  jobs = this.jobService.getJobsByUserId(userId);

        try {
            if(jobs.isEmpty()==true){
                log.error("jobs are empty "); 
                return ResponseEntity.status(404).body(null);
            }    
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
        
        return ResponseEntity.status(201).body(jobs);
    }

    @GetMapping("/search")
    ResponseEntity<?> getAllJobsByJobName(@RequestParam String jobName){

        

        List<Job> jobs = this.jobService.getJobsByJobName(jobName);

        
        if(jobs == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(jobs);
    }

    @PostMapping("/edit")
    ResponseEntity<?> udpdateJob(@RequestBody Job job){

        Job response = this.jobService.updateJob(job);

        
        if(response == null){
            log.error("Error: Updating Job");
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.status(201).body(response);
    }

}
