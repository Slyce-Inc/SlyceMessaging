package it.slyce.messaging.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import it.slyce.messaging.R;

/**
 * Created by John C. Hunchar on 2/2/16.
 */
public class FontEditText extends EditText {
    private static final String VALUE_FONT_DIR_PREFIX = "fonts/";

    public FontEditText(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);

    }

    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    /**
     * Always call this like a constructor
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        String font = null;

        // Load in font attribute
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.FontEditText,
                    defStyleAttr,
                    defStyleRes);

            font = a.getString(R.styleable.FontEditText_editText_font);
            a.recycle();
        }

        // Set font if available
        if (!TextUtils.isEmpty(font)) {
            StringBuilder fontDirBuilder = new StringBuilder();
            fontDirBuilder.append(VALUE_FONT_DIR_PREFIX).append(font);
            try {
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), fontDirBuilder.toString());
                setTypeface(typeface);
            } catch (Exception ignore) {

            }
        }
    }
}
