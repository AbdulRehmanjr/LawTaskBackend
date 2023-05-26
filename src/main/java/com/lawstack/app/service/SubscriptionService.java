package com.lawstack.app.service;

import com.lawstack.app.model.Subscription;
import com.stripe.model.Customer;


public interface SubscriptionService {
    
    
    Subscription addCustomer(String email,String customerId,String SubscrptionId,String DiscountId);

    Subscription getCustomerByEmail(String email);

    void deleteSubscription(String email);

    Subscription updateSubscription(Subscription subscription);

    Customer retrievCustomer(String id);
}
