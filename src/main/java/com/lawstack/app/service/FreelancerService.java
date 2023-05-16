package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.Freelancer;

public interface FreelancerService {
    
    Freelancer saveFreelancer(Freelancer freelancer);

    Freelancer getOneFreelancer(String id);

    List<Freelancer> getTopRatedFreelancers();

    List<Freelancer> getAllFreelancers();

    Freelancer updateRating(String id,double rating);

}
