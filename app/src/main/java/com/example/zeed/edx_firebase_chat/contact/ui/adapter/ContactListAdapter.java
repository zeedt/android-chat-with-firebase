package com.example.zeed.edx_firebase_chat.contact.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zeed.edx_firebase_chat.R;
import com.example.zeed.edx_firebase_chat.contact.ui.interfaces.OnContactClickOperations;
import com.example.zeed.edx_firebase_chat.model.User;
import com.example.zeed.edx_firebase_chat.util.GlideUtil;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private final static String IMAGE_URL = "http://www.gravatar.com/avatar/";

    private List<User> contactList = new ArrayList<>();

    private OnContactClickOperations onContactClickOperations;

    public ContactListAdapter(List<User> contactList, OnContactClickOperations onContactClickOperations) {
        this.contactList = contactList;
        this.onContactClickOperations = onContactClickOperations;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(contactList.get(position).getEmail());
        String status = contactList.get(position).isOnline() ? "Online" : "Offline";
        holder.statusText.setText(status);
        GlideUtil.loadImage(holder.itemView, IMAGE_URL, holder.circleImageView);
        holder.setOnCLickListener(new User(contactList.get(position).isOnline(), contactList.get(position).getEmail()), onContactClickOperations);
    }

    @Override
    public int getItemCount() {
        return  (CollectionUtils.isEmpty(contactList)) ? 0 : contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView statusText;
        private CircleImageView circleImageView;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.statusText = (TextView) itemView.findViewById(R.id.status_text);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.item_image);
            this.view = itemView;
        }

        public void setOnCLickListener(final User user, final OnContactClickOperations contactClickOperations) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactClickOperations.onItemClick(user);
                }
            });
        }
    }

    public void addUser(User user) {
        if (!isUserAlreadyExisting(user)) {
            contactList.add(user);
            this.notifyDataSetChanged();
        }
    }

    public void updateUser(User user){
        int index = getIndexByEmail(user.getEmail());
        contactList.set(index, user);
        this.notifyDataSetChanged();
    }

    public void removeUser(User user){
        int index = getIndexByEmail(user.getEmail());
        contactList.remove(index);
        this.notifyDataSetChanged();
    }

    public boolean isUserAlreadyExisting (User user) {
        for (User existingUser:contactList) {
            if (existingUser.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public int getIndexByEmail(String email) {
        int position = 0;
        for (User user:contactList) {
            if (user.getEmail().equals(email)) {
                break;
            }
            position++;
        }
        return position;
    }

}
