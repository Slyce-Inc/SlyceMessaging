package it.slyce.slyce_messaging.messenger.message;

import android.content.Context;

import it.slyce.slyce_messaging.messenger.message.Message;
import it.slyce.slyce_messaging.messenger.message.messageItem.MessageItem;
import it.slyce.slyce_messaging.messenger.message.messageItem.spinner.SpinnerItem;

/**
 * Created by matthewpage on 7/5/16.
 */
public class SpinnerMessage extends Message {
    @Override
    public MessageItem toMessageItem(Context context) {
        return new SpinnerItem();
    }
}
