package com.example.zeed.edx_firebase_chat.contact.ui.interfaces;

import com.example.zeed.edx_firebase_chat.model.User;

/**
 * Created by zeed on 1/20/19.
 */

public interface ContactListOperations {

    void addContact(User user);
    void updateContact(User user);
    void removeContact(User user);
}
