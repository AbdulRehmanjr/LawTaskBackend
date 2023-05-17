package com.lawstack.app.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.SellerRequest;

public interface SellerRequestRespository extends JpaRepository<SellerRequest,String>{
     
    SellerRequest findByUserUserId(String userId);

    List<SellerRequest> findAllByisActiveFalse();

    List<SellerRequest> findAllByisActiveTrue();
    
}
