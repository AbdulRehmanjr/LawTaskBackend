package com.lawstack.app.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="CHATUSERLIST")
public class ChatUserList {

    @Id
    private String id;

    private String userId;

    private List<String> usersTo = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getUsersTo() {
        return usersTo;
    }

    public void setUsersTo(List<String> usersTo) {
        this.usersTo = usersTo;
    }

    @Override
    public String toString() {
        return "ChatUserList [id=" + id + ", userId=" + userId + ", usersTo=" + usersTo + "]";
    }

  
}
