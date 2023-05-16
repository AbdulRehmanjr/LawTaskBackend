package com.lawstack.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="FREELANCERTABLE")
public class Freelancer {   
    
    @Id
    private String id;

    @OneToOne
    private SellerRequest seller;

    private double rating = 0;

    private double success = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

  

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getSuccess() {
        return success;
    }

    public void setSuccess(double success) {
        this.success = success;
    }

    public SellerRequest getSeller() {
        return seller;
    }

    public void setSeller(SellerRequest seller) {
        this.seller = seller;
    }

}
