package com.example.zeed.edx_firebase_chat.util;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.example.zeed.edx_firebase_chat.db.Database.CONTACTS;
import static com.example.zeed.edx_firebase_chat.db.Database.ONLINE;
import static com.example.zeed.edx_firebase_chat.db.Database.USER_DATABASE;


public class FireBaseHelper {

    private DatabaseReference databaseReference;

    public static String getCurrentlyLoggedInUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = null;
        if (user != null) {
            email = user.getEmail();
        }
        return email;
    }

    public DatabaseReference getMyUSerDatabaseReference() {
        String email = getCurrentlyLoggedInUserEmail();
        if (TextUtils.isEmpty(email)) return null;

        DatabaseReference userDatabaseReference = null;
        userDatabaseReference = databaseReference.getRoot();
        return userDatabaseReference.child(USER_DATABASE).child(getUserKeyFromEmail(email));
    }

    public static String getUserKeyFromEmail(String email) {
        if (TextUtils.isEmpty(email)) return null;
        return email.replace(".", "_");
    }

    public void setUserConnectionStatusOnline() {
        if (getMyUSerDatabaseReference() == null) return;
        Map<String, Object> update = new HashMap<>();
        update.put(ONLINE, true);
        getMyUSerDatabaseReference().updateChildren(update);
        // TODO: implemnet notification of contacts about new status
    }

    public DatabaseReference getUserDatabaseReferenceFromEmail(final String email) {

        return databaseReference.getRoot().child(USER_DATABASE).child(getUserKeyFromEmail(email));
    }

    public DatabaseReference getContactRefrence(String email) {
        return getUserDatabaseReferenceFromEmail(email).child(CONTACTS);
    }

    public static void logoutUser() {
        FirebaseAuth.getInstance().signOut();
    }

    public static FireBaseHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public FireBaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private static class SingletonHolder {
         private static final FireBaseHelper INSTANCE = new FireBaseHelper();
    }

    public DatabaseReference getDatabaseReference(){
        return databaseReference;
    }

    public DatabaseReference getMyContactDatabaseReference() {
        String email = getCurrentlyLoggedInUserEmail();
        return getMyUSerDatabaseReference().child(CONTACTS);
    }

}
