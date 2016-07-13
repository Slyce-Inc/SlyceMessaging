package it.slyce.messaging.message;

import android.content.Context;

import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.externalUser.media.MessageExternalUserMediaItem;
import it.slyce.messaging.message.messageItem.internalUser.media.MessageInternalUserMediaItem;

/**
 * Created by matthewpage on 6/21/16.
 */
public class MediaMessage extends Message {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public MessageItem toMessageItem(Context context){
        if (this.source == MessageSource.EXTERNAL_USER)
            return new MessageExternalUserMediaItem(this, context);
        else
            return new MessageInternalUserMediaItem(this, context);
    }
}