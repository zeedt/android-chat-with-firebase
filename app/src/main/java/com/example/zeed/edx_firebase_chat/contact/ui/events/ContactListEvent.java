package com.example.zeed.edx_firebase_chat.contact.ui.events;

import com.example.zeed.edx_firebase_chat.model.User;

/**
 * Created by zeed on 1/20/19.
 */

public class ContactListEvent {

    private User user;

    private ContactEventType eventType;

    public ContactListEvent(User user, ContactEventType eventType) {
        this.user = user;
        this.eventType = eventType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ContactEventType getEventType() {
        return eventType;
    }

    public void setEventType(ContactEventType eventType) {
        this.eventType = eventType;
    }

    public enum  ContactEventType {
        ADD_CONTACT,
        UPDATE_CONTACT,
        REMOVE_CONTACT
    }

}
