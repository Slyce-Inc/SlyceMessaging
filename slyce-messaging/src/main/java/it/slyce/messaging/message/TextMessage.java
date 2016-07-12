package it.slyce.messaging.message;

import android.content.Context;

import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.scout.text.MessageScoutTextItem;
import it.slyce.messaging.message.messageItem.user.text.MessageUserTextItem;

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
        if (this.origin == MessageSource.EXTERNAL_USER)
            return new MessageScoutTextItem(this, context);
        else
            return new MessageUserTextItem(this, context);
    }
}