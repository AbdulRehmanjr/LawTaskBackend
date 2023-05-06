package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Job;
import com.lawstack.app.model.Seller;
import com.lawstack.app.model.User;
import com.lawstack.app.repository.JobsRepository;
import com.lawstack.app.service.JobService;
import com.lawstack.app.service.SellerService;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobServiceImp  implements JobService{
    

    @Autowired
    private JobsRepository jobRepo;
    
    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    @Override
    public Job createJob(Job job) {

        log.info("Insert new Job  in database");

        final  String userId = job.getUser().getUserId();
        User user = this.userService.getUserById(userId);

        if(user==null){
            log.error("User not found with given userId");
            return null;
        }
        //* check seller has permission to add job or job slot free */
        Seller seller = this.sellerService.getSellerByUserId(userId);

        if(seller.getCurrentJobs()==seller.getMaxJobs()){
            return null;
        }
        
        // setting unique id
        String id = UUID.randomUUID().toString();
        job.setJobId(id);

        seller.setCurrentJobs(seller.getCurrentJobs()+1);
        
        Job response = this.jobRepo.save(job);
        
        if(response!=null){
            this.sellerService.updateJobStatus(seller);   
            return job; 
        }
        return null;

    }

    @Override
    public Job getByJobId(String jobId) {
       log.info("Get job by its id");

       return this.jobRepo.findById(jobId).get();
    }

    @Override
    public List<Job> getJobsByUserId(String userId) {
      log.info("getting all jobs by User id: {}",userId);
        
      List<Job> jobs = this.jobRepo.findAllByUserUserId(userId);
        log.info("{} {}",jobs.size(),jobs.isEmpty());
      if(jobs.isEmpty()==true){
        log.error("Jobs not found");
        return null;
      }
      return jobs;

    }

    @Override
    public Job updateJob(Job job) {
     return null;
    }

    @Override
    public List<Job> getJobsByJobName(String jobName) {
      
      log.info("Fetching all Jobs By Job Name");

      List<Job> jobs = this.jobRepo.findAllByJobNameContains(jobName);

      

      if(jobs.isEmpty()){
        log.error("Jobs not found");
        return null;
      }
      return jobs;
    }
    
}