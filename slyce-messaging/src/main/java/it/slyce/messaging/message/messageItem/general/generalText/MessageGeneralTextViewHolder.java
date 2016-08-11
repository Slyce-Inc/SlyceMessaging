package it.slyce.messaging.message.messageItem.general.generalText;

import android.view.View;
import android.widget.TextView;

import it.slyce.messaging.R;
import it.slyce.messaging.message.messageItem.MessageViewHolder;
import it.slyce.messaging.utils.CustomSettings;

public class MessageGeneralTextViewHolder extends MessageViewHolder {
    public TextView messageTextView;

    public MessageGeneralTextViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView, customSettings);

        messageTextView = (TextView) itemView.findViewById(R.id.message_general_text_text_view);
        messageTextView.setTextColor(customSettings.timestampColor);
    }
}
