package com.lawstack.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Order;

public interface OrderRepository  extends JpaRepository<Order,String>{
    
    List<Order> findAllByUserUserId(String userId);

    List<Order> findAllByCustomerEmail(String email);

    Order findByIdAndConfirmedFalse(String id);

    List<Order> findByCustomerId(String id);
}
