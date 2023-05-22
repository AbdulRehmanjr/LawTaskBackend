package com.lawstack.app.service.implementation;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawstack.app.model.Dashboard;
import com.lawstack.app.model.Subscription;
import com.lawstack.app.repository.SubscriptionRepository;
import com.lawstack.app.service.DashboardService;
import com.lawstack.app.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubscriptionServiceImp implements SubscriptionService{
    
    
    @Autowired
    private SubscriptionRepository subRepo;

    @Override
    public Subscription addCustomer(String email,String customerId,String SubscriptionId,String DiscountId) {
        
        log.info("Saving a new Subscription in database");

        Subscription sub = new Subscription();

        String id = UUID.randomUUID().toString();

        sub.setSubId(id);
        sub.setCustomerId(customerId);
        sub.setEmail(email);
        sub.setSubscriptionId(SubscriptionId);
        sub.setDiscountId(DiscountId);

        LocalDate valid = LocalDate.now().plusDays(30);
        sub.setDateValid(valid);

        try {
            sub = this.subRepo.save(sub);
            return sub;
        } catch (Exception e) {
            log.error("Already existed");
        }
        return null;
        
    }

    @Override
    public Subscription getCustomerByEmail(String email) {
        
        log.info("Geting already existed subscription by email : {}",email);

        Subscription sub = this.subRepo.findByEmail(email);
        return sub;
    }

    @Override
    public Customer retrievCustomer(String id) {
        Customer customer = null;
        try {
             customer = Customer.retrieve(id);
            return customer;
        } catch (StripeException e) {
           log.error("Error: {}",e.getMessage());
        
        }
        return customer;
    }

    @Override
    public void deleteSubscription(String email) {
        log.info("deleting the subscription");

        Subscription response = this.subRepo.findByEmail(email);
        
        response.setSubscriptionId(null);
        

        this.subRepo.save(response);
    }
    
    
}
