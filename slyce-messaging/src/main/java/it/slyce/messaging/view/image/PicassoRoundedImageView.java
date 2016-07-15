package it.slyce.messaging.view.image;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import it.slyce.messaging.BuildConfig;
import it.slyce.messaging.R;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by John C. Hunchar on 2/2/16.
 */
public class PicassoRoundedImageView extends FixedAspectRatioRoundedImageView {
    private static final String TAG = PicassoRoundedImageView.class.getName();

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

    public PicassoRoundedImageView(Context context) {
        super(context);
        init();
    }

    public PicassoRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PicassoRoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
            mPicassoRef = new WeakReference<>(picasso);
            mSourceImageUrl = sourceImageUrl;
            mScaleType = scaleType != null ? scaleType : ScaleType.CENTER_CROP;

            // Load the URL
            loadImageUrl();
        }
    }

    private void loadImageUrl() {
        // If the view has been laid out (has width/height), and hasn't been loaded yet, attempt to load it
        System.out.println("------------------------------");
        System.out.println(mSourceImageUrl);
        if (mViewHasLaidOut && !mHasStartedImageLoad && mPicassoRef != null && !TextUtils.isEmpty(mSourceImageUrl)) {
            Picasso picasso = mPicassoRef.get();
            if (picasso != null && mSourceImageUrl != null) {

                switch (mScaleType) {
                    case CENTER_CROP:
                        Glide
                                .with(getContext())
                                .load(mSourceImageUrl)
                                .centerCrop()
                                .error(R.drawable.shape_rounded_rectangle_gray)
                                .placeholder(R.drawable.shape_rounded_rectangle_gray)
                                .into(this);
                        /*
                        picasso.setLoggingEnabled(true);
                        picasso.load(mSourceImageUrl)
                                .resize(mWidthPx, mHeightPx)
                                .centerCrop()
                                .error(R.drawable.shape_rounded_rectangle_gray)
                                .placeholder(R.drawable.shape_rounded_rectangle_gray)
                                .into(this, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& onSuccess");
                                    }

                                    @Override
                                    public void onError() {
                                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& onError");
                                    }
                                });
                                */
                        break;

                    case FIT:
                        picasso.load(mSourceImageUrl)
                                .fit()
                                .error(R.drawable.shape_rounded_rectangle_gray)
                                .placeholder(R.drawable.shape_rounded_rectangle_gray)
                                .into(this);
                        break;

                    case VARIABLE_HEIGHT:
                        picasso.load(mSourceImageUrl)
                                .resize(mWidthPx, 0)
                                .error(R.drawable.shape_rounded_rectangle_gray)
                                .placeholder(R.drawable.shape_rounded_rectangle_gray)
                                .into(this);
                        break;

                    case VARIABLE_WIDTH:
                        picasso.load(mSourceImageUrl)
                                .resize(0, mHeightPx)
                                .error(R.drawable.shape_rounded_rectangle_gray)
                                .placeholder(R.drawable.shape_rounded_rectangle_gray)
                                .into(this);
                        break;

                    default:
                        picasso.load(mSourceImageUrl)
                                .error(R.drawable.shape_rounded_rectangle_gray)
                                .placeholder(R.drawable.shape_rounded_rectangle_gray)
                                .into(this);
                        break;
                }
            } else {
                setImageResource(R.drawable.shape_rounded_rectangle_gray);
            }
        }
    }
}
