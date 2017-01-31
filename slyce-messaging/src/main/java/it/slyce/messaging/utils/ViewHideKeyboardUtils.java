package it.slyce.messaging.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by pr on 31.01.2017.
 */

public class ViewHideKeyboardUtils {
    public static void RequestKeyboardHide(View view) {
        if (view == null)
            return;

        // hide keyboard please
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
