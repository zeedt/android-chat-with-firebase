package com.example.zeed.edx_firebase_chat.contact.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zeed.edx_firebase_chat.R;
import com.example.zeed.edx_firebase_chat.contact.ui.events.AddContactEvent;
import com.example.zeed.edx_firebase_chat.contact.ui.interfaces.AddContactProcessor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AddContactFragment extends DialogFragment implements DialogInterface.OnShowListener {

    private AddContactProcessor addContactProcessor;

    private EditText editText;

    public AddContactFragment(AddContactProcessor addContactProcessor) {
        super();
        this.addContactProcessor = addContactProcessor;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
        .setTitle(R.string.add_new_contact_title)
        .setPositiveButton(R.string.ok_contact_dialog_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .setNegativeButton(R.string.cancel_contact_dialog_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_contact_fragment, null);
        builder.setView(view);
        editText = ((TextInputLayout) view.findViewById(R.id.new_username)).getEditText();
        AlertDialog dialog =  builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {

        AlertDialog alertDialog = (AlertDialog) getDialog();

        if (alertDialog != null) {
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText.getText() == null || TextUtils.isEmpty(editText.getText().toString())) {
                        Toast.makeText(getActivity(), "Username field cannot be blank", Toast.LENGTH_SHORT).show();
                    } else {
                        addContactProcessor.addContact(editText.getText().toString());
                    }
                }
            });

        }

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addContactEventListener(AddContactEvent addContactEvent) {
        if (addContactEvent.isSuccessful()) {
            Toast.makeText(getActivity(), "Contact added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Contact not successfully added", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
