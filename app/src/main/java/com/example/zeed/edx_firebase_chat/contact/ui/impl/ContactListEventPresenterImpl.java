package com.example.zeed.edx_firebase_chat.contact.ui.impl;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.zeed.edx_firebase_chat.contact.ui.events.ContactListEvent;
import com.example.zeed.edx_firebase_chat.contact.ui.interfaces.ContactListOperations;
import com.example.zeed.edx_firebase_chat.contact.ui.interfaces.ContactListenerPresenter;
import com.example.zeed.edx_firebase_chat.model.User;
import com.example.zeed.edx_firebase_chat.util.FireBaseHelper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ContactListEventPresenterImpl implements ContactListenerPresenter {

    private ChildEventListener contactEventListener;

    private ContactListOperations contactListOperations;

    public ContactListEventPresenterImpl(ContactListOperations contactListOperations) {
        this.contactListOperations = contactListOperations;
    }


    @Override
    public void subscribeForContactListEvents() {

        FireBaseHelper helper = FireBaseHelper.getInstance();

        if (contactEventListener == null) {
            contactEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    EventBus.getDefault().post(new ContactListEvent(getUserObjectFromSnapShot(dataSnapshot), ContactListEvent.ContactEventType.ADD_CONTACT));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    EventBus.getDefault().post(new ContactListEvent(getUserObjectFromSnapShot(dataSnapshot), ContactListEvent.ContactEventType.UPDATE_CONTACT));
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    EventBus.getDefault().post(new ContactListEvent(getUserObjectFromSnapShot(dataSnapshot), ContactListEvent.ContactEventType.REMOVE_CONTACT));

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }

        helper.getMyContactDatabaseReference().addChildEventListener(contactEventListener);
    }

    @Override
    public void unSubscribeForContactListEvents() {

    }

    @Override
    public void destroyContactListEvents() {

    }

    @Override
    public void removeContactListEvents() {

    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void contactEventListener(ContactListEvent contactListEvent) {
        if (contactListEvent.getEventType() == ContactListEvent.ContactEventType.ADD_CONTACT)
            contactListOperations.addContact(contactListEvent.getUser());
        if (contactListEvent.getEventType() == ContactListEvent.ContactEventType.UPDATE_CONTACT)
            contactListOperations.updateContact(contactListEvent.getUser());
        if (contactListEvent.getEventType() == ContactListEvent.ContactEventType.REMOVE_CONTACT)
            contactListOperations.removeContact(contactListEvent.getUser());

    }

    private User getUserObjectFromSnapShot(DataSnapshot snapshot) {
        String email = snapshot.getKey();
        boolean online = ((Boolean) snapshot.getValue()).booleanValue();
        return new User(online, email.replace("_", "."));
    }

}
