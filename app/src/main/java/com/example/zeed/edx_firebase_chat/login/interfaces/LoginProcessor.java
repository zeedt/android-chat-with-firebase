package com.example.zeed.edx_firebase_chat.login.interfaces;

/**
 * Created by zeed on 1/12/19.
 */

public interface LoginProcessor {

    void loginUser(String username, String password);

    void validateCredentials(String email, String password);

    void checkForAuthenticatedLogin();

}
