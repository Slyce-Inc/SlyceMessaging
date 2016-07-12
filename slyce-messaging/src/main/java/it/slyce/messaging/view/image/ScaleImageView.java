package it.slyce.messaging.view.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by John C. Hunchar on 3/14/16.
 */
public class ScaleImageView extends ImageView {
    private static final String TAG = ScaleImageView.class.getName();
    private static final float DEFAULT_WIDTH_TO_HEIGHT_RATIO = 1.0f;

    private float mWidthToHeightRatio;

    public ScaleImageView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        // Set the ratio to the default
        mWidthToHeightRatio = DEFAULT_WIDTH_TO_HEIGHT_RATIO;

        // Get the drawable attributes
        if (mWidthToHeightRatio == DEFAULT_WIDTH_TO_HEIGHT_RATIO) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                float height = drawable.getIntrinsicHeight();
                float width = drawable.getIntrinsicWidth();

                mWidthToHeightRatio = width / height;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
            int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
            int newWidth, newHeight;

            // Only height set to wrap_content, adjust it based on the aspect ratio
            if (layoutParams.height == FrameLayout.LayoutParams.WRAP_CONTENT
                    && layoutParams.width != FrameLayout.LayoutParams.WRAP_CONTENT) {
                int adjustedHeight = (int) (measuredWidth / mWidthToHeightRatio);
                newWidth = measuredWidth;
                newHeight = adjustedHeight;
            }
            // Only width set to wrap_content, adjust it based on the aspect ratio
            else if (layoutParams.height != FrameLayout.LayoutParams.WRAP_CONTENT
                    && layoutParams.width == FrameLayout.LayoutParams.WRAP_CONTENT) {
                int adjustedWidth = (int) (measuredHeight * mWidthToHeightRatio);
                newWidth = adjustedWidth;
                newHeight = measuredHeight;
            }
            else {
                newWidth = measuredWidth;
                newHeight = measuredHeight;
            }

            // Adjust the width/height
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY));
        }
        catch (Exception ignored) {
        }
    }
}
