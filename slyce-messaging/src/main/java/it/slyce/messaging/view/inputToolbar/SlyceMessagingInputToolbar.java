package it.slyce.messaging.view.inputToolbar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.Enumeration;
import java.util.List;

import it.slyce.messaging.SlyceMessagingFragment;

/**
 * Created by pr on 30.01.2017.
 */

public interface SlyceMessagingInputToolbar {
    void inflateInputToolbarViews(Context context, ViewGroup viewGroup);

    void onInputToolbarViewClicked(SlyceMessagingFragment fragment, View viewId);
}
