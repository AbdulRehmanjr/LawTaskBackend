package com.lawstack.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawstack.app.model.SocialLinks;
import com.lawstack.app.repository.SocialRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/social")
@Slf4j
public class SocialController {
    
    @Autowired
    private SocialRepository repo;

    @PostMapping("/save")
    ResponseEntity<?> saveSocialLinks(@RequestBody SocialLinks socialLinks){

        log.info("Saving social links to website");
        SocialLinks link = this.repo.save(socialLinks);

        if(link!=null){
            return ResponseEntity.status(201).body(link);
        }

        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/all")
    ResponseEntity<?> getSocialLinks(){

        log.info("Saving social links to website");
        List<SocialLinks> links = this.repo.findAll();

        if(links!=null){
            return ResponseEntity.status(201).body(links);
        }

        return ResponseEntity.status(404).body(null);
    } 

}
