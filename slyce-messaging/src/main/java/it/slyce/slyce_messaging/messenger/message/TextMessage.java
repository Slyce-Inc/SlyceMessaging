package it.slyce.slyce_messaging.messenger.message;

import android.content.Context;

import it.slyce.slyce_messaging.messenger.message.Message;
import it.slyce.slyce_messaging.messenger.message.MessageSource;
import it.slyce.slyce_messaging.messenger.message.messageItem.MessageItem;
import it.slyce.slyce_messaging.messenger.message.messageItem.scout.text.MessageScoutTextItem;
import it.slyce.slyce_messaging.messenger.message.messageItem.user.text.MessageUserTextItem;

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