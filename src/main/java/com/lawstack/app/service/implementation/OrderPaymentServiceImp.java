package com.lawstack.app.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.OrderPayment;
import com.lawstack.app.repository.OrderPaymentRepository;
import com.lawstack.app.service.OrderPaymentService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderPaymentServiceImp implements OrderPaymentService {

    @Autowired
    private OrderPaymentRepository opRepo;

    @Override
    public OrderPayment saveOrderPayment(OrderPayment payment) {

        log.info("Saving Order payment");
        String id = UUID.randomUUID().toString();

        payment.setId(id);

        return this.opRepo.save(payment);
    }

    @Override
    public List<OrderPayment> getAllByUserId(String userId) {

        List<OrderPayment> payments = this.opRepo.findAllBySellerId(userId);

        try {
            if (payments == null || payments.isEmpty()) {
                log.error("No Payment Found with given userId");
                return null;
            }
        } catch (Exception e) {
                log.error("No Payment Found with given userId");
                return null;
        }

        return payments;
    }

    @Override
    public List<OrderPayment> getAllByCustomerId(String customerId) {

        List<OrderPayment> payments = this.opRepo.findAllByCustomerId(customerId);

        try {
            if (payments == null || payments.isEmpty()) {
                log.error("No Payment Found with given customerId");
                return null;
            }
        } catch (Exception e) {
                log.error("No Payment Found with given customerId");
                return null;
        }

        return payments;
    }

    @Override
    public List<OrderPayment> getAllByCustomerEmail(String customerEmail) {
          List<OrderPayment> payments = this.opRepo.findAllByEmail(customerEmail);

        try {
            if (payments == null || payments.isEmpty()) {
                log.error("No Payment Found with given email");
                return null;
            }
        } catch (Exception e) {
                log.error("No Payment Found with given email");
                return null;
        }

        return payments;
    }

}
