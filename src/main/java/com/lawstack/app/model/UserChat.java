package com.lawstack.app.model;

public class UserChat {
    
    private String id;

    private User receiver;

    private User sender;

    private int unRead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }
}
