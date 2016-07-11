package it.slyce.slyce_messaging.messenger.message.messageItem.master.text;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import it.slyce.slyce_messaging.messenger.utils.CustomSettings;
import it.slyce.slyce_messaging.messenger.message.messageItem.MessageViewHolder;

/**
 * Created by matthewpage on 6/27/16.
 */
public abstract class MessageTextViewHolder extends MessageViewHolder {
    public ImageView carrot;
    public TextView text;
    public FrameLayout bubble;

    public MessageTextViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);
    }
}