package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Freelancer;

public interface FreeLancerRepository extends JpaRepository<Freelancer,String>{

    Freelancer findBySellerUserUserId(String userId);
}
