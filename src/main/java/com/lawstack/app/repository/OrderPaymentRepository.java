package com.lawstack.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.OrderPayment;

public interface OrderPaymentRepository extends JpaRepository<OrderPayment,String>{

    List<OrderPayment>  findAllBySellerId(String sellerId);

    List<OrderPayment> findAllByCustomerId(String customerId);

    List<OrderPayment> findAllByEmail(String email);

}