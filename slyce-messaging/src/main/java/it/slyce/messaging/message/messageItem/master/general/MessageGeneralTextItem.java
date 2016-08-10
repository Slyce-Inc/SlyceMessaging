package it.slyce.messaging.message.messageItem.master.general;

import it.slyce.messaging.message.GeneralTextMessage;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageViewHolder;

/**
 * @Author Matthew Page
 * @Date 8/10/16
 */
public class MessageGeneralTextItem extends MessageItem {
    public MessageGeneralTextItem(GeneralTextMessage message) {
        super(message);
    }

    @Override
    public void buildMessageItem(
            MessageViewHolder messageViewHolder) {
        // TODO
    }

    @Override
    public MessageItemType getMessageItemType() {
        return MessageItemType.GENERAL;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }
}
