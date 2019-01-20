package com.example.zeed.edx_firebase_chat.login.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.example.zeed.edx_firebase_chat.login.eventbus.LoginErrorEvent;
import com.example.zeed.edx_firebase_chat.login.eventbus.LoginSuccessEvent;
import com.example.zeed.edx_firebase_chat.login.eventbus.RecoveryFailEvent;
import com.example.zeed.edx_firebase_chat.login.interfaces.LoginProcessor;
import com.example.zeed.edx_firebase_chat.login.interfaces.LoginView;
import com.example.zeed.edx_firebase_chat.model.User;
import com.example.zeed.edx_firebase_chat.util.FireBaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.zeed.edx_firebase_chat.db.Database.USER_DATABASE;

public class LoginProcessorImpl implements LoginProcessor {

    private Logger logger = Logger.getLogger(LoginProcessorImpl.class.getName());

    private DatabaseReference databaseReference;

    private FireBaseHelper fireBaseHelper;

    private LoginView loginView;

    public LoginProcessorImpl(LoginView loginView) {
        this.loginView = loginView;
        this.fireBaseHelper = FireBaseHelper.getInstance();
    }

    @Override
    public void loginUser(String username, String password) {
        loginView.hideLoginView();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                EventBus.getDefault().post(new LoginSuccessEvent());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                logger.log(Level.INFO, "Error occurred while logging in due to ", e);
                EventBus.getDefault().post(new LoginErrorEvent());
            }
        });

    }


    @Override
    public void validateCredentials(String email, String password) {
        if (email == null || !email.contains("@") || email.length() < 6) {
            this.loginView.setLoginError("Invalid email entered");
            return;
        }
        if (password == null || password.length() < 6){
            this.loginView.setLoginError("Invalid password entered");
            return;
        }
        loginUser(email, password);
    }

    @Override
    public void checkForAuthenticatedLogin() {
        this.loginView.hideLoginView();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null || TextUtils.isEmpty(firebaseUser.getEmail())) {
            EventBus.getDefault().post(new RecoveryFailEvent());
            return;
        }
        getUserDatabaseReferenceWithEmail(firebaseUser.getEmail());
    }

    public void getUserDatabaseReferenceWithEmail(String email) {
        if (TextUtils.isEmpty(email)) return;
        databaseReference = fireBaseHelper.getDatabaseReference();
        databaseReference = databaseReference.getRoot().child(USER_DATABASE).child(FireBaseHelper.getUserKeyFromEmail(email));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadLandingPage(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                logger.log(Level.SEVERE, databaseError.getDetails());
                EventBus.getDefault().post(new RecoveryFailEvent());
            }
        });

    }

    private void loadLandingPage(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        if (user == null) {
            registerUser();
        }
        FireBaseHelper helper = FireBaseHelper.getInstance();
        helper.setUserConnectionStatusOnline();
        EventBus.getDefault().post(new LoginSuccessEvent());
    }

    private void registerUser() {
        String email = FireBaseHelper.getCurrentlyLoggedInUserEmail();
        if (TextUtils.isEmpty(email)) logger.log(Level.WARNING, "Unable to retrieve user's details in user table");
        databaseReference = fireBaseHelper.getDatabaseReference();
        databaseReference.getRoot().child(USER_DATABASE).child(fireBaseHelper.getUserKeyFromEmail(email)).setValue(new User(true, email));
    }

}
