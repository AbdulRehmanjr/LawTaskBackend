package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Category;
import com.lawstack.app.model.Dashboard;
import com.lawstack.app.model.Job;
import com.lawstack.app.model.Seller;
import com.lawstack.app.model.User;
import com.lawstack.app.model.UserDashboard;
import com.lawstack.app.repository.CategoryRepository;
import com.lawstack.app.repository.JobsRepository;
import com.lawstack.app.service.DashboardService;
import com.lawstack.app.service.JobService;
import com.lawstack.app.service.SellerService;
import com.lawstack.app.service.UserDashBoardService;
import com.lawstack.app.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobServiceImp implements JobService {

  @Autowired
  private JobsRepository jobRepo;

  @Autowired
  private UserService userService;

  @Autowired
  private SellerService sellerService;

  @Autowired
  private DashboardService dashService;

  @Autowired
  private UserDashBoardService udService;

  @Autowired
  private CategoryRepository catRepo;

  @Override
  public Job createJob(Job job) {

    

    String userId = job.getUser().getUserId();
    User user = this.userService.getUserById(userId);

    if (user == null) {
      log.error("User not found with given userId");
      return null;
    }
    // * check seller has permission to add job or job slot free */
    Seller seller = this.sellerService.getSellerByUserId(userId);
    Category category = this.catRepo.findById(job.getCategory().getId()).get();
    if (seller.getCurrentJobs() == seller.getMaxJobs() || category  == null) {
      return null;
    }

    //* setting unique id
    String id = UUID.randomUUID().toString();
    job.setJobId(id);
    job.setCategory(category);

    category.setJobCount(category.getJobCount()+1);

    this.catRepo.save(category);
    seller.setCurrentJobs(seller.getCurrentJobs() + 1);
    
    Job response = this.jobRepo.save(job);

    if (response != null) {
      this.sellerService.updateJobStatus(seller);
      Dashboard dashboard = new Dashboard();
      UserDashboard udash = new UserDashboard();

      udash.setJobs(1);
      udash.setUserId(user.getUserId());
      this.udService.updateDashboard(udash);
      dashboard.setJobs(1);
      this.dashService.updateDashboard(dashboard);
      return response;
    }
    return null;

  }

  @Override
  public Job getByJobId(String jobId) {
    log.info("Get job by its id");
    Job job = null;
    try {
       job = this.jobRepo.findById(jobId).get();
    } catch (Exception e) {
      log.error("Error : {}",e.getMessage());
      return null;
    }
    return job;
  }

  @Override
  public List<Job> getJobsByUserId(String userId) {
    

    List<Job> jobs = this.jobRepo.findAllByUserUserId(userId);

    if (jobs == null) {
      log.error("Getting all jobs by User id: {}", userId);
      return null;
    }
    return jobs;
  }

  @Override
  public Job updateJob(Job job) {

    Job response = this.jobRepo.findById(job.getJobId()).get();

    if(response == null){
      log.error("Cant fetch job with id : {}",job.getJobId());
      return null;
    }

    response.setJobName(job.getJobName());
    response.setJobPrice(job.getJobPrice());
    response.setJobType(job.getJobType());
    response.setDescription(job.getDescription());
    return this.jobRepo.save(response);

  }

  @Override
  public List<Job> getJobsByJobName(String jobName) {

    List<Job> jobs = this.jobRepo.findAllByJobNameContains(jobName);

    try {
      if (jobs.isEmpty()) {
        log.error("Jobs not found");
        return null;
      }
    } catch (Exception e) {
      log.error("Error : {}", e.getMessage());
      return null;
    }
    return jobs;
  }

  @Override
  public List<Job> getJobsByCategoryName(String categoryName) {
    
    List<Job> jobs = this.jobRepo.findAllByCategoryNameIgnoreCaseContains(categoryName);

    try {
      if (jobs.isEmpty()) {
        log.error("Jobs not found");
        return null;
      }
    } catch (Exception e) {
      log.error("Error : {}", e.getMessage());
      return null;
    }
    return jobs;
  }

  @Override
  public List<Job> getJobsByCategoryId(int id) {

    List<Job> jobs = this.jobRepo.findAllByCategoryId(id);

    try {
      if (jobs.isEmpty()) {
        log.error("Jobs not found");
        return null;
      }
    } catch (Exception e) {
      log.error("Error : {}", e.getMessage());
      return null;
    }
    return jobs;
  }

  @Override
  public List<Job> getAll() {

    return this.jobRepo.findAll();
    
  }

}
