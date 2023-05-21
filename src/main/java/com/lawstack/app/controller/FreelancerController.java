package com.lawstack.app.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.Freelancer;
import com.lawstack.app.service.FreelancerService;

@RestController
@RequestMapping("/freelancer")
public class FreelancerController {
    
    @Autowired
    private FreelancerService flService;



    @GetMapping("/top")
    ResponseEntity<?> getTopRated(){

        List<Freelancer> freelancers = this.flService.getTopRatedFreelancers();

        if(freelancers == null){
            ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(freelancers);
    }


    @GetMapping("/search")
    ResponseEntity<?> getFreelancerById(@RequestParam String freelancerId){


        Freelancer freelancer = this.flService.getOneFreelancer(freelancerId);

        if(freelancer == null){
            ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(201).body(freelancer);
    }
}
