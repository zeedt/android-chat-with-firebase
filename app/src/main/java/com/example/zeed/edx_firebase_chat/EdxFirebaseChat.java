package com.example.zeed.edx_firebase_chat;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by zeed on 1/13/19.
 */

public class EdxFirebaseChat extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
