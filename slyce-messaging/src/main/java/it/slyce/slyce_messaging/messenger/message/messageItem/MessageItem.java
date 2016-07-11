package it.slyce.slyce_messaging.messenger.message.messageItem;

import android.view.View.OnClickListener;

import it.slyce.slyce_messaging.messenger.message.Message;
import it.slyce.slyce_messaging.messenger.message.MessageSource;
import it.slyce.slyce_messaging.messenger.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by John C. Hunchar on 5/12/16.
 */
public abstract class MessageItem {

    protected boolean firstConsecutiveMessageFromSource;
    protected boolean lastConsecutiveMessageFromSource;

    protected String avatarUrl;
    protected String initials;
    protected Message message;
    protected String date;

    public MessageItem(Message messageData) {
        this.message = messageData;
    }

    public abstract void buildMessageItem(
            MessageViewHolder messageViewHolder,
            Picasso picasso,
            OnClickListener onClickListener);

    public abstract MessageItemType getMessageItemType();

    public abstract MessageSource getMessageSource();

    public abstract String getMessageLink();

    public Message getMessage() {
        return message;
    }

    public int getMessageItemTypeOrdinal() {
        return getMessageItemType().ordinal();
    }

    public void setFirstConsecutiveMessageFromSource(boolean firstConsecutiveMessageFromSource) {
        this.firstConsecutiveMessageFromSource = firstConsecutiveMessageFromSource;
    }

    public void setLastConsecutiveMessageFromSource(boolean lastConsecutiveMessageFromSource) {
        this.lastConsecutiveMessageFromSource = lastConsecutiveMessageFromSource;
    }

    public String getDate() {
        return date;
    }

    public void updateDate(long time) {
        this.date = Utils.getTimestamp(time);
    }
}
