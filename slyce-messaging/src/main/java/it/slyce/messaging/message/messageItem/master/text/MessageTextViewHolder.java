package it.slyce.messaging.message.messageItem.master.text;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import it.slyce.messaging.utils.CustomSettings;
import it.slyce.messaging.message.messageItem.MessageViewHolder;

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