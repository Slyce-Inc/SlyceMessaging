package it.slyce.messaging.message.messageItem.general.generalText;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
        messageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hide keyboard please
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }
}
