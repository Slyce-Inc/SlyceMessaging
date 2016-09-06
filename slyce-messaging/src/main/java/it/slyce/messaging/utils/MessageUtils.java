package it.slyce.messaging.utils;

import java.util.List;

import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;

/**
 * @Author Matthew Page
 * @Date 7/13/16
 */
public class MessageUtils {

    public static void markMessageItemAtIndexIfFirstOrLastFromSource(int i, List<MessageItem> messageItems) {
        if (i < 0 || i >= messageItems.size()) {
            return;
        }

        MessageItem messageItem = messageItems.get(i);
        messageItem.setIsFirstConsecutiveMessageFromSource(false);
        messageItem.setIsLastConsecutiveMessageFromSource(false);

        if (previousMessageIsNotFromSameSource(i, messageItems)) {
            messageItem.setIsFirstConsecutiveMessageFromSource(true);
        }

        if (isTheLastConsecutiveMessageFromSource(i, messageItems)) {
            messageItem.setIsLastConsecutiveMessageFromSource(true);
        }
    }

    private static boolean previousMessageIsNotFromSameSource(int i, List<MessageItem> messageItems) {
        return true;
    }

    private static boolean isTheLastConsecutiveMessageFromSource(int i, List<MessageItem> messageItems) {
        if (isSpinnerMessage(i, messageItems)) {
            return false;
        }

        return i == messageItems.size() - 1 ||
                nextMessageHasDifferentSource(i, messageItems) ||
                messageItems.get(i).getMessage() == null ||
                messageItems.get(i + 1).getMessage() == null ||
                nextMessageWasAtLeastAnHourAfterThisOne(i, messageItems);
    }

    private static boolean nextMessageWasAtLeastAnHourAfterThisOne(int i, List<MessageItem> messageItems) {
        return messageItems.get(i + 1).getMessage().getDate() - messageItems.get(i).getMessage().getDate() > 1000 * 60 * 60; // one hour
    }

    private static boolean nextMessageHasDifferentSource(int i, List<MessageItem> messageItems) {
        return messageItems.get(i).getMessageSource() != messageItems.get(i + 1).getMessageSource();
    }

    private static boolean isSpinnerMessage(int i, List<MessageItem> messageItems) {
        return messageItems.get(i).getMessageItemType() == MessageItemType.SPINNER;
    }
}
