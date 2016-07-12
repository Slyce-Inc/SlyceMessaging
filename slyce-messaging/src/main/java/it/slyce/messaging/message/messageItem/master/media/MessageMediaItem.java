package it.slyce.messaging.message.messageItem.master.media;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import it.slyce.messaging.message.MediaMessage;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.utils.Utils;
import it.slyce.messaging.ViewImageActivity;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageViewHolder;
import it.slyce.messaging.view.image.PicassoRoundedImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by matthewpage on 6/27/16.
 */
public abstract class MessageMediaItem extends MessageItem {
    private MediaMessage mediaMessage;
    private Context context;

    public MessageMediaItem(MediaMessage mediaMessage, Context context) {
        super(mediaMessage);
        this.context = context;
        this.mediaMessage = mediaMessage;
    }

    @Override
    public void buildMessageItem(
            MessageViewHolder messageViewHolder,
            Picasso picasso,
            View.OnClickListener onClickListener) {

        if (mediaMessage != null &&  messageViewHolder != null && messageViewHolder instanceof MessageMediaViewHolder) {
            final MessageMediaViewHolder messageMediaViewHolder = (MessageMediaViewHolder) messageViewHolder;

            // Get content
            float widthToHeightRatio = Utils.getWidthToHeightRatio(mediaMessage.getUrl(), context);
            date = Utils.getTimestamp(mediaMessage.getDate());
            final String mediaUrl = mediaMessage.getUrl();
            this.avatarUrl = mediaMessage.getAvatarUrl();

            // Populate views with content
            messageMediaViewHolder.timestamp.setText(date != null ? date : "");
            messageMediaViewHolder.initials.setText(initials != null ? initials : "");

            messageMediaViewHolder.media.setWidthToHeightRatio(widthToHeightRatio);
            messageMediaViewHolder.media.setImageUrlToLoadOnLayout(picasso, mediaUrl, PicassoRoundedImageView.ScaleType.CENTER_CROP);

            if (picasso != null && firstConsecutiveMessageFromSource) {
                picasso.load(avatarUrl).into(messageMediaViewHolder.avatar);
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

            messageMediaViewHolder.avatar.setVisibility(firstConsecutiveMessageFromSource && !TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.INVISIBLE);
            messageMediaViewHolder.avatarContainer.setVisibility(firstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            messageMediaViewHolder.initials.setVisibility(firstConsecutiveMessageFromSource && TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.GONE);
            messageMediaViewHolder.media.setVisibility(!TextUtils.isEmpty(mediaUrl) && picasso != null ? View.VISIBLE : View.INVISIBLE);
            messageMediaViewHolder.timestamp.setVisibility(lastConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public MessageItemType getMessageItemType() {
        if (mediaMessage.getOrigin() == MessageSource.EXTERNAL_USER)
            return MessageItemType.SCOUT_MEDIA;
        else
            return MessageItemType.USER_MEDIA;
    }

    @Override
    public String getMessageLink() {
        return null;
    }

    @Override
    public MessageSource getMessageSource() {
        return mediaMessage.getOrigin();
    }

    public MediaMessage getMediaMessage() {
        return mediaMessage;
    }

    public boolean dateNeedsUpdated(long time) {
        return Utils.dateNeedsUpdated(time, date);
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }
}
