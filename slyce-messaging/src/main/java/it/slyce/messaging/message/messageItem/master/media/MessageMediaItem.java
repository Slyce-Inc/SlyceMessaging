package it.slyce.messaging.message.messageItem.master.media;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;

import it.slyce.messaging.message.MediaMessage;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.utils.MediaUtils;
import it.slyce.messaging.utils.DateUtils;
import it.slyce.messaging.ViewImageActivity;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageViewHolder;

/**
 * Created by matthewpage on 6/27/16.
 */
public abstract class MessageMediaItem extends MessageItem {
    private Context context;

    public MessageMediaItem(MediaMessage mediaMessage, Context context) {
        super(mediaMessage);
        this.context = context;
    }

    @Override
    public void buildMessageItem(
            MessageViewHolder messageViewHolder) {

        if (message != null &&  messageViewHolder != null && messageViewHolder instanceof MessageMediaViewHolder) {

            final MessageMediaViewHolder messageMediaViewHolder = (MessageMediaViewHolder) messageViewHolder;

            // Get content
            float widthToHeightRatio = MediaUtils.getWidthToHeightRatio(getMediaMessage().getUrl(), context);
            date = DateUtils.getTimestamp(context, message.getDate());
            final String mediaUrl = getMediaMessage().getUrl();
            this.avatarUrl = message.getAvatarUrl();

            // Populate views with content
            messageMediaViewHolder.timestamp.setText(date != null ? date : "");
            messageMediaViewHolder.initials.setText(initials != null ? initials : "");

            messageMediaViewHolder.media.setWidthToHeightRatio(widthToHeightRatio);
            messageMediaViewHolder.media.setImageUrlToLoadOnLayout(mediaUrl);

            if (isFirstConsecutiveMessageFromSource) {
                Glide.with(context).load(avatarUrl).into(messageMediaViewHolder.avatar);
            }

            messageViewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    if (messageMediaViewHolder.customSettings.userClicksAvatarPictureListener != null)
                        messageMediaViewHolder.customSettings.userClicksAvatarPictureListener.userClicksAvatarPhoto(message.getUserId());
                }
            });

            messageMediaViewHolder.media.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ViewImageActivity.class);
                    intent.putExtra("URL", mediaUrl);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            messageMediaViewHolder.avatar.setVisibility(isFirstConsecutiveMessageFromSource && !TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.INVISIBLE);
            messageMediaViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            messageMediaViewHolder.initials.setVisibility(isFirstConsecutiveMessageFromSource && TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.GONE);
            messageMediaViewHolder.media.setVisibility(!TextUtils.isEmpty(mediaUrl) ? View.VISIBLE : View.INVISIBLE);
            messageMediaViewHolder.timestamp.setVisibility(isLastConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public MessageItemType getMessageItemType() {
        if (message.getSource() == MessageSource.EXTERNAL_USER) {
            return MessageItemType.INCOMING_MEDIA;
        } else {
            return MessageItemType.OUTGOING_MEDIA;
        }
    }

    @Override
    public MessageSource getMessageSource() {
        return message.getSource();
    }

    public MediaMessage getMediaMessage() {
        return (MediaMessage)message;
    }

    public boolean dateNeedsUpdated(long time) {
        return DateUtils.dateNeedsUpdated(context, time, date);
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }
}
