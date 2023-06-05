package com.lawstack.app.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="FREELANCERTABLE")
public class Freelancer {   
    
    @Id
    private String id;

    @OneToOne(fetch = FetchType.EAGER)
    private SellerRequest seller;

    private int rating = 0;

    private int success = 100;

    private List<String> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

  

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public SellerRequest getSeller() {
        return seller;
    }

    public void setSeller(SellerRequest seller) {
        this.seller = seller;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

}
