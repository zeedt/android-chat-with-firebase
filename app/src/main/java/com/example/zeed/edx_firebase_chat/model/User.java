package com.example.zeed.edx_firebase_chat.model;

import java.util.Map;

/**
 * Created by zeed on 1/13/19.
 */

public class User {

    boolean online = false;

    String email;

    Map<String, Boolean> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(Map<String, Boolean> contactPersons) {
        this.contactPersons = contactPersons;
    }

    private Map<String, Boolean> contactPersons;

    public User(boolean online, String email) {
        this.online = online;
        this.email = email;
    }

    public User() {
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
