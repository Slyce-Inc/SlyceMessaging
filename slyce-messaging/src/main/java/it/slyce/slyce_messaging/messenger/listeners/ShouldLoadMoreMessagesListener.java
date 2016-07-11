package it.slyce.slyce_messaging.messenger.listeners;

import java.util.List;

import it.slyce.slyce_messaging.messenger.message.Message;

/**
 * Created by matthewpage on 7/1/16.
 */
public interface ShouldLoadMoreMessagesListener {
    public List<Message> shouldLoadMoreMessages();
}
