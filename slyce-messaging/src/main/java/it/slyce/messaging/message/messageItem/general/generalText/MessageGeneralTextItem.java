package it.slyce.messaging.message.messageItem.general.generalText;

import it.slyce.messaging.message.GeneralTextMessage;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageViewHolder;

public class MessageGeneralTextItem extends MessageItem {
    public MessageGeneralTextItem(GeneralTextMessage message) {
        super(message);
    }

    @Override
    public void buildMessageItem(
            MessageViewHolder messageViewHolder) {
        MessageGeneralTextViewHolder viewHolder = (MessageGeneralTextViewHolder) messageViewHolder;
        GeneralTextMessage generalTextMessage = (GeneralTextMessage) message;
        viewHolder.messageTextView.setText(generalTextMessage.getText());
    }

    @Override
    public MessageItemType getMessageItemType() {
        return MessageItemType.GENERAL_TEXT;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }
}
