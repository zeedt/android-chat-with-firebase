package com.example.zeed.edx_firebase_chat.chat.interfaces;

import com.example.zeed.edx_firebase_chat.model.Chat;

/**
 * Created by zeed on 1/21/19.
 */

public interface ChatProcessor {

    void sendMessage(Chat chat);

    void onMessageReceived();

    void subscribeToChatEventListener(String email);

}
