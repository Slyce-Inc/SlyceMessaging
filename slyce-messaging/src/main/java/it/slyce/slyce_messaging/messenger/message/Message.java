package it.slyce.slyce_messaging.messenger.message;

import android.content.Context;

import it.slyce.slyce_messaging.messenger.message.messageItem.MessageItem;

/**
 * Created by matthewpage on 6/21/16.
 */
public abstract class Message {
    long date;
    MessageSource origin;
    String avatarUrl;
    String displayName;
    String userId;
    String initials;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public MessageSource getOrigin() {
        return origin;
    }

    public void setOrigin(MessageSource origin) {
        this.origin = origin;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public abstract MessageItem toMessageItem(Context context);
}
