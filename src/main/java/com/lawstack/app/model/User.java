package com.lawstack.app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;

import jakarta.persistence.Lob;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "USERTABLE")
public class User {

    @Id
    private String userId;

    private String userName;

    @Column(unique = true)
    private String email;
    
    private String password;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Seller seller;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Job> job;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private SellerRequest sellerRequest;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<WithDraw> withDraws;

   
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public List<Job> getJob() {
        return job;
    }

    public void setJob(List<Job> job) {
        this.job = job;
    }

}
