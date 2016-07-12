package it.slyce.messaging.message;

import android.content.Context;

import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.spinner.SpinnerItem;

/**
 * Created by matthewpage on 7/5/16.
 */
public class SpinnerMessage extends Message {
    @Override
    public MessageItem toMessageItem(Context context) {
        return new SpinnerItem();
    }
}
