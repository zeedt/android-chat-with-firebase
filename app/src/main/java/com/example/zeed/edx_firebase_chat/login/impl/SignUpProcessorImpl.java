package com.example.zeed.edx_firebase_chat.login.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.zeed.edx_firebase_chat.login.eventbus.SignupFailureEvent;
import com.example.zeed.edx_firebase_chat.login.eventbus.SignupSuccessEvent;
import com.example.zeed.edx_firebase_chat.login.interfaces.LoginView;
import com.example.zeed.edx_firebase_chat.login.interfaces.SignupProcessor;
import com.example.zeed.edx_firebase_chat.model.User;
import com.example.zeed.edx_firebase_chat.util.FireBaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.zeed.edx_firebase_chat.db.Database.USER_DATABASE;

/**
 * Created by zeed on 1/13/19.
 */

public class SignUpProcessorImpl implements SignupProcessor {
    private static final Logger LOGGER = Logger.getLogger(SignUpProcessorImpl.class.getName());
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private LoginView loginView;

    public SignUpProcessorImpl(LoginView loginView) {
        this.loginView = loginView;
    }


    @Override
    public void signUp(final String email, String password) {

        FireBaseHelper helper = FireBaseHelper.getInstance();

        final DatabaseReference databaseReference = helper.getDatabaseReference();

        loginView.hideLoginView();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                databaseReference.getRoot().child(USER_DATABASE).child(FireBaseHelper.getUserKeyFromEmail(email)).setValue(new User(false, email), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        LOGGER.log(Level.INFO, "completed");
                    }
                });
                EventBus.getDefault().post(new SignupSuccessEvent());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                EventBus.getDefault().post(new SignupFailureEvent(e.getMessage()));
            }
        });

    }
}
