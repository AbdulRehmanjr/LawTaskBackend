package com.lawstack.app.service;

import java.util.List;

import com.lawstack.app.model.OrderPayment;

public interface OrderPaymentService {
    
    OrderPayment saveOrderPayment(OrderPayment payment);

    List<OrderPayment> getAllByUserId(String userId);

    List<OrderPayment> getAllByCustomerId(String customerId);

    List<OrderPayment> getAllByCustomerEmail(String customerEmail);
}
