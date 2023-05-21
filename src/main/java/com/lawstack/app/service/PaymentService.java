package com.lawstack.app.service;

import com.lawstack.app.model.Order;

public interface PaymentService {
   
    String paymentCheckout(String type,String email);

    String projectPayment(Order order);
}
