package com.example.zeed.edx_firebase_chat.login.ui;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.zeed.edx_firebase_chat.R;
import com.example.zeed.edx_firebase_chat.contact.ui.ContactActivity;
import com.example.zeed.edx_firebase_chat.login.eventbus.LoginErrorEvent;
import com.example.zeed.edx_firebase_chat.login.eventbus.LoginSuccessEvent;
import com.example.zeed.edx_firebase_chat.login.eventbus.RecoveryFailEvent;
import com.example.zeed.edx_firebase_chat.login.eventbus.SignupFailureEvent;
import com.example.zeed.edx_firebase_chat.login.eventbus.SignupSuccessEvent;
import com.example.zeed.edx_firebase_chat.login.impl.LoginProcessorImpl;
import com.example.zeed.edx_firebase_chat.login.impl.SignUpProcessorImpl;
import com.example.zeed.edx_firebase_chat.login.interfaces.LoginProcessor;
import com.example.zeed.edx_firebase_chat.login.interfaces.LoginView;
import com.example.zeed.edx_firebase_chat.login.interfaces.SignupProcessor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private TextInputLayout email;

    private TextInputLayout password;

    private LoginProcessor loginProcessor;

    private SignupProcessor signupProcessor;

    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        scrollView = findViewById(R.id.login_form);

        this.loginProcessor = new LoginProcessorImpl(this);
        this.signupProcessor = new SignUpProcessorImpl(this);
        EventBus.getDefault().register(this);
        getSupportActionBar().hide();

        this.loginProcessor.checkForAuthenticatedLogin();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void setLoginError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginView() {
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoginView() {
        scrollView.setVisibility(View.INVISIBLE);
    }

    public void login(View view) {

        if (isInputValid())
            this.loginProcessor.validateCredentials(email.getEditText().getText().toString(), password.getEditText().getText().toString());

    }

    public void signup(View view) {
        this.signupProcessor.signUp(email.getEditText().getText().toString(), password.getEditText().getText().toString());
    }

    private boolean isInputValid() {

        if (email == null || email.getEditText() == null || email.getEditText().getText() == null) return false;
        if (password == null || password.getEditText() == null || password.getEditText().getText() == null ) return false;

        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSuccessEvent(LoginSuccessEvent successEvent) {
        startActivity(new Intent(this,ContactActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleErrorEvent(LoginErrorEvent errorEvent) {
        showLoginView();
        setLoginError("Unable to login successful");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSignUpSuccessEvent(SignupSuccessEvent successEvent) {
        showLoginView();
        Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUserRecoveryFailEvent(RecoveryFailEvent recoveryFailEvent) {
        showLoginView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleSignUpErrorEvent(SignupFailureEvent errorEvent) {
        showLoginView();
        setLoginError(errorEvent.getErrorMessage());
    }

}
