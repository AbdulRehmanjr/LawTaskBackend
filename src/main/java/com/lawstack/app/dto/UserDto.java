package com.lawstack.app.dto;

import com.lawstack.app.model.Role;

public class UserDto {
    
    private String userId;

    private String userName;

    private String email;
    
    private byte[] profilePicture;

    private Role role;

    public UserDto() {
    }

    public UserDto(String userId, String userName, String email,byte[] profilePicture, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.profilePicture = profilePicture;
        this.role = role;
    }

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
