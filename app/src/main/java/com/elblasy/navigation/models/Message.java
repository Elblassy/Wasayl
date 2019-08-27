package com.elblasy.navigation.models;

public class Message {
    private String message, title, token, sender;
    private boolean isSelf;

    public Message() {
    }

    public Message(String message, String title, boolean isSelf, String token, String sender) {
        this.message = message;
        this.title = title;
        this.isSelf = isSelf;
        this.sender = sender;
        this.token = token;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}
