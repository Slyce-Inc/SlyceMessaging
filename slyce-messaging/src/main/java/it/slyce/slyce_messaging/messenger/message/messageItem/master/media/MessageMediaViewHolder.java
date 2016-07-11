package it.slyce.slyce_messaging.messenger.message.messageItem.master.media;

import android.view.View;

import it.slyce.slyce_messaging.messenger.utils.CustomSettings;
import it.slyce.slyce_messaging.messenger.message.messageItem.MessageViewHolder;
import it.slyce.slyce_messaging.view.image.PicassoRoundedImageView;

/**
 * Created by matthewpage on 6/27/16.
 */
public class MessageMediaViewHolder extends MessageViewHolder {
    public PicassoRoundedImageView media;

    public MessageMediaViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);
    }
}
