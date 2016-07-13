package it.slyce.messaging.message.messageItem.master.text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.TextMessage;
import it.slyce.messaging.utils.DateUtils;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageViewHolder;
import com.squareup.picasso.Picasso;

/**
 * Created by matthewpage on 6/27/16.
 */
public class MessageTextItem extends MessageItem {
    private TextMessage textMessage;
    private Context context;
    private String avatarUrl;

    public MessageTextItem(TextMessage textMessage, Context context) {
        super(textMessage);
        this.textMessage = textMessage;
        this.context = context;
    }

    @Override
    public void buildMessageItem(
            MessageViewHolder messageViewHolder,
            final Picasso picasso,
            View.OnClickListener onClickListener) {

        if (textMessage != null &&  messageViewHolder != null && messageViewHolder instanceof MessageTextViewHolder) {
            final MessageTextViewHolder messageTextViewHolder = (MessageTextViewHolder) messageViewHolder;

            // Get content
            String date = DateUtils.getTimestamp(textMessage.getDate());
            String text = textMessage.getText();
            this.avatarUrl = textMessage.getAvatarUrl();
            this.initials = textMessage.getInitials();

            // Populate views with content
            messageTextViewHolder.initials.setText(initials  != null ? initials : "");
            messageTextViewHolder.text.setText(text != null ? text : "");
            messageTextViewHolder.timestamp.setText(date != null ? date : "");

            messageTextViewHolder.bubble.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager)
                    context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", ((TextMessage)MessageTextItem.this.message).getText());
                    clipboard.setPrimaryClip(clip);
                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(150);
                    Toast.makeText(context, "Text copied", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            messageViewHolder.avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageTextViewHolder.customSettings.userClicksAvatarPictureListener != null)
                        messageTextViewHolder.customSettings.userClicksAvatarPictureListener.userClicksAvatarPhoto(message.getUserId());
                }
            });

            if (picasso != null && firstConsecutiveMessageFromSource) {
                picasso.load(avatarUrl).into(messageTextViewHolder.avatar);
            }

            messageTextViewHolder.avatar.setVisibility(firstConsecutiveMessageFromSource && !TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.INVISIBLE);
            messageTextViewHolder.avatarContainer.setVisibility(firstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            messageTextViewHolder.carrot.setVisibility(firstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            messageTextViewHolder.initials.setVisibility(firstConsecutiveMessageFromSource && TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.GONE);
            messageTextViewHolder.timestamp.setVisibility(lastConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public MessageItemType getMessageItemType() {
        if (textMessage.getOrigin() == MessageSource.EXTERNAL_USER)
            return MessageItemType.SCOUT_TEXT;
        else
            return MessageItemType.USER_TEXT;
    }

    @Override
    public String getMessageLink() {
        return null;
    }

    @Override
    public MessageSource getMessageSource() {
        return textMessage.getOrigin();
    }
}
