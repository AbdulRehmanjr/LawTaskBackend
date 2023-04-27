package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Seller;

public interface SellerRespository extends JpaRepository<Seller,String>{
     
    Seller findByUserId(String userId);
}
