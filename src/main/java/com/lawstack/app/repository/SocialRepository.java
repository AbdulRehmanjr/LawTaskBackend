package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.SocialLinks;

public interface SocialRepository extends JpaRepository<SocialLinks,Integer>{
    
}
