package it.slyce.messaging.message;

import android.content.Context;

import it.slyce.messaging.listeners.OnOptionSelectedListener;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.general.generalOptions.MessageGeneralOptionsItem;

public class GeneralOptionsMessage extends Message {
    private String title;
    private String[] options;
    private OnOptionSelectedListener onOptionSelectedListener;
    private boolean selected;
    private String finalText;

    public GeneralOptionsMessage() {
        this.selected = false;
    }

    public OnOptionSelectedListener getOnOptionSelectedListener() {
        return onOptionSelectedListener;
    }

    public void setOnOptionSelectedListener(OnOptionSelectedListener onOptionSelectedListener) {
        this.onOptionSelectedListener = onOptionSelectedListener;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public MessageItem toMessageItem(Context context) {
        return new MessageGeneralOptionsItem(this, context);
    }

    public void setSelected() {
        this.selected = true;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setFinalText(String finalText) {
        this.finalText = finalText;
    }

    public String getFinalText() {
        return finalText;
    }
}
