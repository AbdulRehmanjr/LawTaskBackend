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

    private String UserId;

    private List<String> UsersTo = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public List<String> getUsersTo() {
        return UsersTo;
    }

    public void setUsersTo(List<String> usersTo) {
        UsersTo = usersTo;
    }

    @Override
    public String toString() {
        return "ChatUserList [id=" + id + ", UserId=" + UserId + ", UsersTo=" + UsersTo + "]";
    }
}
