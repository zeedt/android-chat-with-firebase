package com.example.zeed.edx_firebase_chat.chat.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zeed.edx_firebase_chat.R;
import com.example.zeed.edx_firebase_chat.model.Chat;
import com.example.zeed.edx_firebase_chat.util.FireBaseHelper;

import java.util.List;

/**
 * Created by zeed on 1/22/19.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public List<Chat> chatList;

    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.messageView.setText(chatList.get(position).getMessage());
        boolean sentByMe = isSentByMe(chatList.get(position).getSender());

        if (sentByMe) {
            holder.messageView.setGravity(Gravity.RIGHT);
            holder.messageView.setBackgroundColor(Color.parseColor("lime"));
        } else {
            holder.messageView.setGravity(Gravity.LEFT);
            holder.messageView.setBackgroundColor(Color.parseColor("silver"));
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.messageView = itemView.findViewById(R.id.chat_message);
        }
    }

    public void addChat(Chat chat) {
        chatList.add(chat);
        notifyDataSetChanged();
    }

    public boolean isSentByMe(String sender) {
        return sender.equalsIgnoreCase(FireBaseHelper.getCurrentlyLoggedInUserEmail());
    }

}
