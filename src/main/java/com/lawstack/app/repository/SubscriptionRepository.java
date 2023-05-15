package com.lawstack.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription,String>{

    Subscription findByEmail(String email);
}
