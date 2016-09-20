package it.slyce.messaging.message.messageItem.master.text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import it.slyce.messaging.R;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.TextMessage;
import it.slyce.messaging.utils.DateUtils;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageItemType;
import it.slyce.messaging.message.messageItem.MessageViewHolder;

/**
 * Created by matthewpage on 6/27/16.
 */
public class MessageTextItem extends MessageItem {
    private Context context;
    private String avatarUrl;

    public MessageTextItem(TextMessage textMessage, Context context) {
        super(textMessage);
        this.context = context;
    }

    @Override
    public void buildMessageItem(
            MessageViewHolder messageViewHolder) {

        if (message != null &&  messageViewHolder != null && messageViewHolder instanceof MessageTextViewHolder) {
            final MessageTextViewHolder messageTextViewHolder = (MessageTextViewHolder) messageViewHolder;

            // Get content
            String date = DateUtils.getTimestamp(context, message.getDate());
            String text = ((TextMessage)message).getText();
            this.avatarUrl = message.getAvatarUrl();
            this.initials = message.getInitials();

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
                    String toastMessage = (String) context.getText(R.string.message_text_copied);
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
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

            if (isFirstConsecutiveMessageFromSource) {
                Glide.with(context).load(avatarUrl).into(messageTextViewHolder.avatar);
            }

            messageTextViewHolder.avatar.setVisibility(isFirstConsecutiveMessageFromSource && !TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.INVISIBLE);
            messageTextViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            messageTextViewHolder.carrot.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            messageTextViewHolder.initials.setVisibility(isFirstConsecutiveMessageFromSource && TextUtils.isEmpty(avatarUrl) ? View.VISIBLE : View.GONE);
            messageTextViewHolder.timestamp.setVisibility(isLastConsecutiveMessageFromSource ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public MessageItemType getMessageItemType() {
        if (message.getSource() == MessageSource.EXTERNAL_USER) {
            return MessageItemType.INCOMING_TEXT;
        } else {
            return MessageItemType.OUTGOING_TEXT;
        }
    }

    @Override
    public MessageSource getMessageSource() {
        return message.getSource();
    }
}
