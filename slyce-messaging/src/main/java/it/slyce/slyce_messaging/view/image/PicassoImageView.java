package it.slyce.slyce_messaging.view.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import it.slyce.slyce_messaging.BuildConfig;
import it.slyce.slyce_messaging.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

/**
 * Created by John C. Hunchar on 2/2/16.
 */
public class PicassoImageView extends FixedAspectRatioImageView {
    private static final String TAG = PicassoImageView.class.getName();

    private boolean mHasStartedImageLoad;
    private boolean mViewHasLaidOut;
    private int mHeightPx;
    private int mWidthPx;
    private ScaleType mScaleType;
    private String mSourceImageUrl;
    private WeakReference<Picasso> mPicassoRef;

    public enum ScaleType {
        CENTER_CROP,
        FIT,
        VARIABLE_HEIGHT,
        VARIABLE_WIDTH
    }

    public PicassoImageView(Context context) {
        super(context);
        init();
    }

    public PicassoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PicassoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PicassoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * Always call this like a constructor
     */
    private void init() {
        mScaleType = null;
        mHasStartedImageLoad = false;
        mViewHasLaidOut = false;
        mSourceImageUrl = null;
        mPicassoRef = null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // Track that the view has been laid out
        mViewHasLaidOut = true;

        // Get dimensions
        mWidthPx = getMeasuredWidth();
        mHeightPx = getMeasuredHeight();

        // Attempt to load the image URL
        loadImageUrl();
    }

    public void setImageUrlToLoadOnLayout(Picasso picasso, String sourceImageUrl, ScaleType scaleType) {

        // If the image URL is new, attempt to load it
        if (mSourceImageUrl == null || !mSourceImageUrl.equals(sourceImageUrl)) {
            mPicassoRef = new WeakReference<Picasso>(picasso);
            mSourceImageUrl = sourceImageUrl;
            mScaleType = scaleType != null ? scaleType : ScaleType.CENTER_CROP;

            // Load the URL
            loadImageUrl();
        }
    }

    private void loadImageUrl() {
        // If the view has been laid out (has width/height), and hasn't been loaded yet, attempt to load it
        if (mViewHasLaidOut && !mHasStartedImageLoad && mPicassoRef != null && !TextUtils.isEmpty(mSourceImageUrl)) {
            Picasso picasso = mPicassoRef.get();
            if (picasso != null && mSourceImageUrl != null) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "loadImageUrl: mSourceImageUrl = " + mSourceImageUrl);
                }

                switch (mScaleType) {
                    case CENTER_CROP:
                        picasso.load(mSourceImageUrl)
                                .resize(mWidthPx, mHeightPx)
                                .centerCrop()
                                .error(R.drawable.shape_rectangle_gray)
                                .placeholder(R.drawable.shape_rectangle_gray)
                                .into(this);
                        break;

                    case FIT:
                        picasso.load(mSourceImageUrl)
                                .fit()
                                .error(R.drawable.shape_rectangle_gray)
                                .placeholder(R.drawable.shape_rectangle_gray)
                                .into(this);
                        break;

                    case VARIABLE_HEIGHT:
                        picasso.load(mSourceImageUrl)
                                .resize(mWidthPx, 0)
                                .error(R.drawable.shape_rectangle_gray)
                                .placeholder(R.drawable.shape_rectangle_gray)
                                .into(this);
                        break;

                    case VARIABLE_WIDTH:
                        picasso.load(mSourceImageUrl)
                                .resize(0, mHeightPx)
                                .error(R.drawable.shape_rectangle_gray)
                                .placeholder(R.drawable.shape_rectangle_gray)
                                .into(this);
                        break;

                    default:
                        picasso.load(mSourceImageUrl)
                                .error(R.drawable.shape_rectangle_gray)
                                .placeholder(R.drawable.shape_rectangle_gray)
                                .into(this);
                        break;
                }
            } else {
                setImageResource(R.drawable.shape_rectangle_gray);
            }
        }
    }
}
