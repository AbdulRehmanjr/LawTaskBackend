package com.lawstack.app.service;

import com.lawstack.app.model.Subscription;
import com.stripe.model.Customer;


public interface SubscriptionService {
    
    
    Subscription addCustomer(String email,String customerId);

    Subscription getCustomerByEmail(String email);

    Customer retrievCustomer(String id);
}
