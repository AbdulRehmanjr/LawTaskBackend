package com.lawstack.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;

import jakarta.persistence.Lob;

import jakarta.persistence.ManyToOne;

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

}
