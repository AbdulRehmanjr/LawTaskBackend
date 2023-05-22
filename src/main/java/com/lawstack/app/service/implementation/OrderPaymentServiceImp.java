package com.lawstack.app.service.implementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.lawstack.app.model.OrderPayment;
import com.lawstack.app.repository.OrderPaymentRepository;
import com.lawstack.app.service.OrderPaymentService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderPaymentServiceImp  implements OrderPaymentService{
    

    @Autowired
    private OrderPaymentRepository opRepo;
    @Override
    public OrderPayment saveOrderPayment(OrderPayment payment) {
       
        String id = UUID.randomUUID().toString();

        payment.setId(id);

        return this.opRepo.save(payment);
    }
    
}
