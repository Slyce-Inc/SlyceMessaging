package it.slyce.messaging;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.slyce.messaging.message.MediaMessage;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.TextMessage;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.utils.MessageUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by audrey on 7/14/16.
 */
public class MessageUtilsTest {

    List<MessageItem> messageItems;

    @Before
    public void before() {
        messageItems = new ArrayList<>();

        // incoming text
        TextMessage firstIncoming = new TextMessage();
        firstIncoming.setSource(MessageSource.EXTERNAL_USER);
        firstIncoming.setText("hi");
        messageItems.add(firstIncoming.toMessageItem(null));
        // incoming text
        TextMessage secondIncoming = new TextMessage();
        secondIncoming.setSource(MessageSource.EXTERNAL_USER);
        secondIncoming.setText("second message");
        messageItems.add(secondIncoming.toMessageItem(null));
        // incoming image
        MediaMessage incomingImage = new MediaMessage();
        incomingImage.setSource(MessageSource.EXTERNAL_USER);
        incomingImage.setUrl("http://snipsnap.it/fakeimage.png");
        messageItems.add(incomingImage.toMessageItem(null));
        // outgoing text
        TextMessage firstOutgoing = new TextMessage();
        firstOutgoing.setSource(MessageSource.LOCAL_USER);
        firstOutgoing.setText("hi");
        messageItems.add(firstOutgoing.toMessageItem(null));
        // outgoing image
        TextMessage secondOutgoing = new TextMessage();
        secondOutgoing.setSource(MessageSource.LOCAL_USER);
        secondOutgoing.setText("hi");
        messageItems.add(secondOutgoing.toMessageItem(null));
        // incoming text
        TextMessage finalIncoming = new TextMessage();
        finalIncoming.setSource(MessageSource.EXTERNAL_USER);
        finalIncoming.setText("final message");
        messageItems.add(finalIncoming.toMessageItem(null));
    }

    @Test
    public void firstConsecutiveMessageIsMarkedAsFirst() throws Exception {
        MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(0, messageItems);
        MessageItem item = messageItems.get(0);

        assertTrue(item.isFirstConsecutiveMessageFromSource());
        assertFalse(item.isLastConsecutiveMessageFromSource());
    }

    @Test
    public void middleMessageIsNeitherFirstNorLast() throws Exception {
        MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(1, messageItems);
        MessageItem item = messageItems.get(1);

        assertFalse(item.isFirstConsecutiveMessageFromSource());
        assertFalse(item.isLastConsecutiveMessageFromSource());
    }

    @Test
    public void lastIncomingMessageIsMarkedLast() throws Exception {
        MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(2, messageItems);
        MessageItem item = messageItems.get(2);

        assertFalse(item.isFirstConsecutiveMessageFromSource());
        assertTrue(item.isLastConsecutiveMessageFromSource());
    }

    @Test
    public void firstOutgoingMessageIsMarkedFirst() throws Exception {
        MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(3, messageItems);
        MessageItem item = messageItems.get(3);

        assertTrue(item.isFirstConsecutiveMessageFromSource());
        assertFalse(item.isLastConsecutiveMessageFromSource());
    }

    @Test
    public void lastOutgoingMessageIsMarkedLast() throws Exception {
        MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(4, messageItems);
        MessageItem item = messageItems.get(4);

        assertFalse(item.isFirstConsecutiveMessageFromSource());
        assertTrue(item.isLastConsecutiveMessageFromSource());
    }

    @Test
    public void lastMessageIsMarkedLast() throws Exception {
        MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(5, messageItems);
        MessageItem item = messageItems.get(5);

        assertTrue(item.isFirstConsecutiveMessageFromSource());
        assertTrue(item.isLastConsecutiveMessageFromSource());
    }
}