package it.slyce.messaging.view.text;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import it.slyce.messaging.R;

/**
 * Created by John C. Hunchar on 2/2/16.
 */
public class FontTextView extends TextView {
    private static final String VALUE_FONT_DIR_PREFIX = "fonts/";

    public FontTextView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);

    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
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
                    R.styleable.FontTextView,
                    defStyleAttr,
                    defStyleRes);

            font = a.getString(R.styleable.FontTextView_font);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TextView widget = (TextView) this;
        Object text = widget.getText();
        if (text instanceof Spanned) {
            Spannable buffer = (Spannable) text;

            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off,
                        ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                    }
                    return true;
                }
            }

        }

        return false;
    }
}
