package com.lawstack.app.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="SELLERTABLE")
public class Seller {
    
    @Id
    private String sellerId;

    @OneToOne
    private User user;

    
    private int currentJobs = 0;

    
    private int maxJobs = 0;

    
    private String sellerType ="NONE";
    
    private boolean active=false;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCurrentJobs() {
        return currentJobs;
    }

    public void setCurrentJobs(int currentJobs) {
        this.currentJobs = currentJobs;
    }

    public int getMaxJobs() {
        return maxJobs;
    }

    public void setMaxJobs(int maxJobs) {
        this.maxJobs = maxJobs;
    }

    public String getSellerType() {
        return sellerType;
    }

    public void setSellerType(String sellerType) {
        this.sellerType = sellerType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    

}
