package com.lawstack.app.service.implementation;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Freelancer;
import com.lawstack.app.repository.FreeLancerRepository;
import com.lawstack.app.service.FreelancerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FreelancerServiceImp implements FreelancerService {

    @Autowired
    private FreeLancerRepository flRepo;

    @Override
    public Freelancer saveFreelancer(Freelancer freelancer) {

        log.info("Saving the freelancer in database");

        String id = UUID.randomUUID().toString();

        freelancer.setId(id);

        return this.flRepo.save(freelancer);
    }

    @Override
    public Freelancer getOneFreelancer(String id) {

        log.info("Fetching the Freelancer by id : {}", id);

        return this.flRepo.findById(id).get();
    }

    @Override
    public List<Freelancer> getTopRatedFreelancers() {
        log.info("Fetching all freelacners");

        List<Freelancer> freelancers = this.flRepo.findAll();

        if (freelancers != null) {
            freelancers = freelancers.stream()
                    .sorted(Comparator.comparing(Freelancer::getRating))
                    .limit(10)
                    .collect(Collectors.toList());
            return freelancers;
        }

        return null;
    }

    @Override
    public List<Freelancer> getAllFreelancers() {
        log.info("Fetching all freelacners");

        return this.flRepo.findAll();
    }

    @Override
    public Freelancer updateRating(String id,double rating) {
        
        Freelancer freelancer  = this.flRepo.findById(id).get();

        if(freelancer!=null){
            double updatedRating = (freelancer.getRating() + rating)/5.0;

            freelancer.setRating(updatedRating);

            this.flRepo.save(freelancer);

            return freelancer;
        }

        return null;
    }

}
