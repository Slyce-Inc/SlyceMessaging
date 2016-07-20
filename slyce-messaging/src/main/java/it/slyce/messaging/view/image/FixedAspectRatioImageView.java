package it.slyce.messaging.view.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import it.slyce.messaging.R;

/**
 * Created by John C. Hunchar on 3/2/16.
 */
public class FixedAspectRatioImageView extends ImageView {
    private static final float DEFAULT_WIDTH_TO_HEIGHT_RATIO = 1.0f;

    private float mWidthToHeightRatio;

    public FixedAspectRatioImageView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public FixedAspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public FixedAspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public FixedAspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        // Set the ratio to the default
        mWidthToHeightRatio = DEFAULT_WIDTH_TO_HEIGHT_RATIO;

        // Load in any set attributes
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.FixedAspectRatioImageView,
                    defStyleAttr,
                    defStyleRes);

            mWidthToHeightRatio = a.getFloat(R.styleable.FixedAspectRatioImageView_widthToHeightRatio,
                    DEFAULT_WIDTH_TO_HEIGHT_RATIO);
            a.recycle();
        }

        invalidate();
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
        } catch (RuntimeException exception) {
            Log.d("debug", exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void setWidthToHeightRatio(float widthToHeightRatio) {
        mWidthToHeightRatio = widthToHeightRatio;
    }
}
