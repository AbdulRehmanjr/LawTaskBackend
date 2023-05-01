package com.lawstack.app.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Seller;

public interface SellerRepository  extends JpaRepository<Seller,String>{
    
    Seller findByUserUserId(String userId);

}   
