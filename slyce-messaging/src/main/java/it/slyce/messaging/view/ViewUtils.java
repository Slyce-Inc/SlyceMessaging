package it.slyce.messaging.view;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by John C. Hunchar on 2/17/16.
 */
public class ViewUtils {
    public static String getStringFromEditText(EditText editText) {
        String string = null;

        if (editText != null) {
            Editable editable = editText.getText();
            if ((!TextUtils.isEmpty(editable))) {
                string = editable.toString();
            }
        }

        return string;
    }
}
