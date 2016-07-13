package it.slyce.messaging.message;

import android.content.Context;

import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.externalUser.text.MessageExternalUserTextItem;
import it.slyce.messaging.message.messageItem.internalUser.text.MessageInternalUserTextItem;

/**
 * Created by matthewpage on 6/21/16.
 */
public class TextMessage extends Message {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public MessageItem toMessageItem(Context context){
        if (this.source == MessageSource.EXTERNAL_USER)
            return new MessageExternalUserTextItem(this, context);
        else
            return new MessageInternalUserTextItem(this, context);
    }
}