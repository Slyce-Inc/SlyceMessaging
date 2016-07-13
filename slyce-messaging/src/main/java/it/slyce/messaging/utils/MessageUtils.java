package it.slyce.messaging.utils;

import java.util.List;

import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;

/**
 * @Author Matthew Page
 * @Date 7/13/16
 */
public class MessageUtils {
    public static void setFirstOrLast(int i, List<MessageItem> messageItems) {
        if (i < 0 || i >= messageItems.size()) // avoid a NullPointerException
            return;
        MessageItem messageItem = messageItems.get(i);
        messageItem.setFirstConsecutiveMessageFromSource(false);
        messageItem.setLastConsecutiveMessageFromSource(false);
        if (isFirst(i, messageItems))
            messageItem.setFirstConsecutiveMessageFromSource(true);
        if (isLast(i, messageItems))
            messageItem.setLastConsecutiveMessageFromSource(true);
    }

    private static boolean isFirst(int i, List<MessageItem> messageItems) {
        return i == 0 ||
                messageItems.get(i - 1).getMessageItemType() == MessageItemType.SPINNER ||
                messageItems.get(i - 1).getMessageSource() != messageItems.get(i).getMessageSource();
    }

    private static boolean isLast(int i, List<MessageItem> messageItems) {
        if (messageItems.get(i).getMessageItemType() == MessageItemType.SPINNER)
            return false;
        return i == messageItems.size() - 1 ||
                messageItems.get(i).getMessageSource() != messageItems.get(i + 1).getMessageSource() ||
                messageItems.get(i).getMessage() == null ||
                messageItems.get(i + 1).getMessage() == null ||
                messageItems.get(i + 1).getMessage().getDate() - messageItems.get(i).getMessage().getDate() > 1000 * 60 * 60; // one hour
    }
}
