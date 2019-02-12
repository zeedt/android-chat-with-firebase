package com.example.zeed.edx_firebase_chat.chat.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.zeed.edx_firebase_chat.chat.interfaces.ChatOperations;
import com.example.zeed.edx_firebase_chat.chat.interfaces.ChatProcessor;
import com.example.zeed.edx_firebase_chat.model.Chat;
import com.example.zeed.edx_firebase_chat.util.FireBaseHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import static com.example.zeed.edx_firebase_chat.db.Database.CHATS;

public class ChatProcessorImpl implements ChatProcessor {

    private ChildEventListener childEventListener;

    public ChatOperations chatOperations;

    public ChatProcessorImpl(ChatOperations chatOperations) {
        this.chatOperations = chatOperations;
    }

    @Override
    public void sendMessage(Chat chat) {
        FireBaseHelper helper = FireBaseHelper.getInstance();
        final DatabaseReference databaseReference = helper.getChatReference(chat.getSender(), chat.getReceiver());
        databaseReference.push().setValue(chat);
    }

    @Override
    public void onMessageReceived() {

    }

    @Override
    public void subscribeToChatEventListener(String email) {
        FireBaseHelper helper = FireBaseHelper.getInstance();
        String chatKey = helper.getChatMessagePairKeyWithSenderAndReceiver(FireBaseHelper.getCurrentlyLoggedInUserEmail(), email);

        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    chatOperations.onChatAdded(chat);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            helper.getDatabaseReference().getRoot().child(CHATS).child(chatKey).addChildEventListener(childEventListener);
        }


    }
}
