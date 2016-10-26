package it.slyce.messaging.message.messageItem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import it.slyce.messaging.utils.CustomSettings;

/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView timestamp;
    public CustomSettings customSettings;

    public MessageViewHolder(View itemView, CustomSettings customSettings) {
        super(itemView);
        this.customSettings = customSettings;
    }
}
