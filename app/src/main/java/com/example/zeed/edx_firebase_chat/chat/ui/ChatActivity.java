package com.example.zeed.edx_firebase_chat.chat.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.zeed.edx_firebase_chat.R;
import com.example.zeed.edx_firebase_chat.chat.adapter.ChatAdapter;
import com.example.zeed.edx_firebase_chat.chat.impl.ChatProcessorImpl;
import com.example.zeed.edx_firebase_chat.chat.interfaces.ChatOperations;
import com.example.zeed.edx_firebase_chat.chat.interfaces.ChatProcessor;
import com.example.zeed.edx_firebase_chat.model.Chat;
import com.example.zeed.edx_firebase_chat.util.FireBaseHelper;
import com.example.zeed.edx_firebase_chat.util.GeneralUtil;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ChatOperations {

    private ActionBar actionBar;

    private ImageView sendImage;
    private AppCompatEditText editText;
    private String receiver;
    private ChatAdapter adapter;
    private RecyclerView recyclerView;

    private ChatProcessor chatProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);


        actionBar = getSupportActionBar();
        Intent intent = getIntent();
        setUpToolbar(intent);

        sendImage = findViewById(R.id.send_button);
        editText = (AppCompatEditText) findViewById(R.id.message);
        chatProcessor = new ChatProcessorImpl(this);

        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        setUpRecyclerView();


    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.chat_recycler_view);
        adapter= new ChatAdapter(new ArrayList<Chat>());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscribeForChat();
    }

    private void setUpToolbar(Intent intent) {
        receiver = intent.getStringExtra(GeneralUtil.EMAIL_KEY);
        actionBar.setTitle(receiver);
        //TODO : Set Icon appropriately
    }


    @Override
    public void sendMessage() {
        if (editText.getText() != null && !TextUtils.isEmpty(editText.getText().toString())) {
            chatProcessor.sendMessage(new Chat(FireBaseHelper.getCurrentlyLoggedInUserEmail(), receiver, editText.getText().toString()));
            editText.setText("");
        }
    }

    @Override
    public void subscribeForChat() {
        chatProcessor.subscribeToChatEventListener(receiver);
    }

    @Override
    public void onChatAdded(Chat chat) {
        adapter.addChat(chat);
    }
}
