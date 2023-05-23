package com.lawstack.app.service.implementation;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.lawstack.app.model.Subscription;
import com.lawstack.app.repository.SubscriptionRepository;

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
    public Subscription addCustomer(String email, String customerId, String subscriptionId, String discountId) {
        log.info("Saving a new Subscription in the database");
    
        Subscription existingSubscription = this.subRepo.findByEmail(email);
        if (existingSubscription != null) {
            // Subscription already exists, update it instead
            existingSubscription.setCustomerId(customerId);
            existingSubscription.setSubscriptionId(subscriptionId);
            existingSubscription.setDiscountId(discountId);
            existingSubscription.setDateValid(LocalDate.now().plusDays(30));
    
            try {
                Subscription updatedSubscription = this.subRepo.save(existingSubscription);
                return updatedSubscription;
            } catch (Exception e) {
                log.error("Error occurred while updating the subscription: {}", e.getMessage());
            }
        } else {
            // Create a new subscription entry
            Subscription newSubscription = new Subscription();
            newSubscription.setSubId(UUID.randomUUID().toString());
            newSubscription.setEmail(email);
            newSubscription.setCustomerId(customerId);
            newSubscription.setSubscriptionId(subscriptionId);
            newSubscription.setDiscountId(discountId);
            newSubscription.setDateValid(LocalDate.now().plusDays(30));
    
            try {
                Subscription savedSubscription = this.subRepo.save(newSubscription);
                return savedSubscription;
            } catch (Exception e) {
                log.error("Error occurred while saving the subscription: {}", e.getMessage());
            }
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
