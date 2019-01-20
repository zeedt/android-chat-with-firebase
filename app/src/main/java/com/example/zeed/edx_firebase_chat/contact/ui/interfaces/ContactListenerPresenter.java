package com.example.zeed.edx_firebase_chat.contact.ui.interfaces;

/**
 * Created by zeed on 1/19/19.
 */

public interface ContactListenerPresenter {

    void subscribeForContactListEvents();
    void unSubscribeForContactListEvents();
    void destroyContactListEvents();
    void removeContactListEvents();
    void onCreate();
    void onDestroy();

}
