package com.example.zeed.edx_firebase_chat.contact.ui.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.zeed.edx_firebase_chat.contact.ui.events.AddContactEvent;
import com.example.zeed.edx_firebase_chat.contact.ui.interfaces.AddContactProcessor;
import com.example.zeed.edx_firebase_chat.model.User;
import com.example.zeed.edx_firebase_chat.util.FireBaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import static com.example.zeed.edx_firebase_chat.db.Database.USER_DATABASE;
import static com.example.zeed.edx_firebase_chat.util.FireBaseHelper.getUserKeyFromEmail;

/**
 * Created by zeed on 1/15/19.
 */

public class AddContactProcessorImpl implements AddContactProcessor {

    @Override
    public void addContact(final String email) {
        if (TextUtils.isEmpty(email)) return;
        final String seachEmailKey = getUserKeyFromEmail(email);
        final FireBaseHelper helper = FireBaseHelper.getInstance();
        final DatabaseReference databaseReference = helper.getDatabaseReference().getRoot().child(USER_DATABASE).child(seachEmailKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AddContactEvent addContactEvent = new AddContactEvent();
                User user = dataSnapshot.getValue(User.class);
                if (user == null || TextUtils.isEmpty(user.getEmail())) {
                    addContactEvent.setSuccessful(false);
                } else {
                    boolean online = user.isOnline();
                    DatabaseReference userDatabaseReference = helper.getMyContactDatabaseReference();
                    userDatabaseReference.child(getUserKeyFromEmail(email)).setValue(online); //Sets the new status of the currently added user

                    String currentUserEmail = FireBaseHelper.getCurrentlyLoggedInUserEmail();
                    DatabaseReference addedUserContactReference =helper.getContactRefrence(email);
                    addedUserContactReference.child(getUserKeyFromEmail(currentUserEmail)).setValue(true);
                    addContactEvent.setSuccessful(true);
                }

                EventBus.getDefault().post(addContactEvent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
