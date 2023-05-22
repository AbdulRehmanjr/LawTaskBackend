package com.lawstack.app.model;

import java.sql.Date;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="SUBSCRIPTIONTABLE")
public class Subscription {
    
    @Id
    private String subId;

    private String customerId;

    private String email;

    @Column(unique=true,nullable = true)
    private String subscriptionId;

    private String discountId;
    

    @CreationTimestamp
    private Date DateSubcribed;

    private LocalDate DateValid;

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public Date getDateSubcribed() {
        return DateSubcribed;
    }

    public void setDateSubcribed(Date dateSubcribed) {
        DateSubcribed = dateSubcribed;
    }


    public void setDateValid(LocalDate dateValid) {
        DateValid = dateValid;
    }

    public LocalDate getDateValid() {
        return DateValid;
    }

}
