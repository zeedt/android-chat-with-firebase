package com.example.zeed.edx_firebase_chat.chat.interfaces;

import com.example.zeed.edx_firebase_chat.model.Chat;

/**
 * Created by zeed on 1/21/19.
 */

public interface ChatOperations {

    void sendMessage();

    void subscribeForChat();

    void onChatAdded(Chat chat);

}
