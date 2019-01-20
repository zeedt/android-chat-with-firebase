package com.example.zeed.edx_firebase_chat.login.eventbus;

/**
 * Created by zeed on 1/13/19.
 */

public class SignupFailureEvent {

    private String errorMessage;

    public SignupFailureEvent(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
