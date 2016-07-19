package it.slyce.messaging.message.messageItem.master.media;

import android.view.View;

import it.slyce.messaging.utils.CustomSettings;
import it.slyce.messaging.message.messageItem.MessageViewHolder;
import it.slyce.messaging.view.image.GlideRoundedImageView;

/**
 * Created by matthewpage on 6/27/16.
 */
public class MessageMediaViewHolder extends MessageViewHolder {
    public GlideRoundedImageView media;

    public MessageMediaViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);
    }
}
