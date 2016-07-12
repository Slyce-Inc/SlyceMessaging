package it.slyce.messaging.view.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Added by John C. Hunchar on 2/25/16.
 * http://stackoverflow.com/questions/18952271/android-imageview-bottomcrop-instead-of-centercrop
 */
public class BottomCropImageView extends ImageView {
    private static final String TAG = BottomCropImageView.class.getName();

    public BottomCropImageView(Context context) {
        super(context);
        init();
    }

    public BottomCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomCropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomCropImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return super.setFrame(l, t, r, b);
        }

        Matrix matrix = getImageMatrix();

        float scale;

        int viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int viewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();

        // Get the scale
        if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            scale = (float) viewHeight / (float) drawableHeight;
        } else {
            scale = (float) viewWidth / (float) drawableWidth;
        }

        // Define the rect to take image portion from
        RectF drawableRect = new RectF(0, 0, drawableWidth, viewHeight / scale);
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);

        setImageMatrix(matrix);
        return super.setFrame(l, t, r, b);
    }
}
