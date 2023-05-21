package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Coupon;

public interface CouponRepository extends JpaRepository<Coupon,String>{
    
    Coupon findByName(String name);
}
