package com.example.zeed.edx_firebase_chat.contact.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.zeed.edx_firebase_chat.R;
import com.example.zeed.edx_firebase_chat.chat.ui.ChatActivity;
import com.example.zeed.edx_firebase_chat.contact.ui.adapter.ContactListAdapter;
import com.example.zeed.edx_firebase_chat.contact.ui.fragments.AddContactFragment;
import com.example.zeed.edx_firebase_chat.contact.ui.impl.AddContactProcessorImpl;
import com.example.zeed.edx_firebase_chat.contact.ui.impl.ContactListEventPresenterImpl;
import com.example.zeed.edx_firebase_chat.contact.ui.interfaces.ContactListOperations;
import com.example.zeed.edx_firebase_chat.contact.ui.interfaces.ContactListenerPresenter;
import com.example.zeed.edx_firebase_chat.contact.ui.interfaces.OnContactClickOperations;
import com.example.zeed.edx_firebase_chat.login.ui.LoginActivity;
import com.example.zeed.edx_firebase_chat.model.User;
import com.example.zeed.edx_firebase_chat.util.FireBaseHelper;
import com.example.zeed.edx_firebase_chat.util.GeneralUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class ContactActivity extends AppCompatActivity implements ContactListOperations, OnContactClickOperations {

    private RecyclerView recyclerView;

    private ContactListAdapter contactListAdapter;

    private RecyclerView.LayoutManager layoutManager;

    private ContactListenerPresenter contactListenerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddContactFragment addContactFragment = new AddContactFragment(new AddContactProcessorImpl());
                addContactFragment.show(getSupportFragmentManager(), "ADD_CONTACT_FRAGMENT");
            }
        });
        contactListenerPresenter = new ContactListEventPresenterImpl(this);
        contactListenerPresenter.onCreate();;
        recyclerView = (RecyclerView) findViewById(R.id.contact_recycler_view);
        setUpRecyclerViewAdapter();
    }

    private void setUpRecyclerViewAdapter() {
        contactListAdapter = new ContactListAdapter(new ArrayList<User>(), this);
        recyclerView.setAdapter(contactListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        contactListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactListenerPresenter.subscribeForContactListEvents();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FireBaseHelper.logoutUser();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addContact(User user) {
        contactListAdapter.addUser(user);
    }

    @Override
    public void updateContact(User user) {
        contactListAdapter.updateUser(user);
    }

    @Override
    public void removeContact(User user) {
        contactListAdapter.removeUser(user);
    }

    @Override
    public void onItemClick(User user) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(GeneralUtil.EMAIL_KEY, user.getEmail());
        intent.putExtra(GeneralUtil.ONLINE_STATUS_KEY, user.isOnline());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(User user) {

    }
}
