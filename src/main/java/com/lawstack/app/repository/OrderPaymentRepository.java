package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.OrderPayment;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment,String>{
    
}
