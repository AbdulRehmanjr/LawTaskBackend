package com.lawstack.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Job;

public interface JobsRepository extends JpaRepository<Job,String>{

    List<Job> findAllByUserUserId(String userId);

    List<Job> findAllByJobNameContains(String jobName);
    
    List<Job> findAllByCategoryNameIgnoreCaseContains(String categoryName);

    List<Job> findAllByCategoryId(int id);
}
