package com.example.zeed.edx_firebase_chat.model;

import com.google.firebase.database.Exclude;

/**
 * Created by zeed on 1/21/19.
 */

public class Chat {

    private String sender;

    private String receiver;

    private String message;

    @Exclude
    private boolean setByMe;

    public Chat(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Chat() {
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSetByMe() {
        return setByMe;
    }

    public void setSetByMe(boolean setByMe) {
        this.setByMe = setByMe;
    }
}
