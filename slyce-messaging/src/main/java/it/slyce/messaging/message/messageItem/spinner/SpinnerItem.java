package it.slyce.messaging.message.messageItem.spinner;

import android.view.View;

import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageViewHolder;
import com.squareup.picasso.Picasso;

/**
 * Created by matthewpage on 7/5/16.
 */
public class SpinnerItem extends MessageItem {
    public SpinnerItem() {
        super(null);
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder, Picasso picasso) {

    }

    @Override
    public MessageItemType getMessageItemType() {
        return MessageItemType.SPINNER;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.EXTERNAL_USER;
    }

    @Override
    public String getMessageLink() {
        return null;
    }
}
