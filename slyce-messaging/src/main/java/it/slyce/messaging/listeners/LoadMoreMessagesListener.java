package it.slyce.messaging.listeners;

import java.util.List;

import it.slyce.messaging.message.Message;

/**
 * Created by matthewpage on 7/1/16.
 */
public interface LoadMoreMessagesListener {
    public List<Message> loadMoreMessages();
}
