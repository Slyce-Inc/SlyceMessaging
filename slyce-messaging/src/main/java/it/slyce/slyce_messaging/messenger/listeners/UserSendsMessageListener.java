package it.slyce.slyce_messaging.messenger.listeners;

import android.net.Uri;

/**
 * Created by matthewpage on 6/21/16.
 */
public interface UserSendsMessageListener {
    public void onUserSendsTextMessage(String text);
    public void onUserSendsMediaMessage(Uri imageUri);
}
