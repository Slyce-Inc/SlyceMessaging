package it.slyce.messaging.view.image;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import it.slyce.messaging.R;

import com.bumptech.glide.Glide;

/**
 * Created by John C. Hunchar on 2/2/16.
 */
public class GlideRoundedImageView extends FixedAspectRatioRoundedImageView {

    private boolean mHasStartedImageLoad;
    private boolean mViewHasLaidOut;
    private String mSourceImageUrl;

    public GlideRoundedImageView(Context context) {
        super(context);
        init();
    }

    public GlideRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GlideRoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Always call this like a constructor
     */
    private void init() {
        mHasStartedImageLoad = false;
        mViewHasLaidOut = false;
        mSourceImageUrl = null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // Track that the view has been laid out
        mViewHasLaidOut = true;

        // Attempt to load the image URL
        loadImageUrl();
    }

    public void setImageUrlToLoadOnLayout(String sourceImageUrl) {

        // If the image URL is new, attempt to load it
        if (mSourceImageUrl == null || !mSourceImageUrl.equals(sourceImageUrl)) {
            mSourceImageUrl = sourceImageUrl;
            // Load the URL
            loadImageUrl();
        }
    }

    private void loadImageUrl() {
        // If the view has been laid out (has width/height), and hasn't been loaded yet, attempt to load it
        if (mViewHasLaidOut && !mHasStartedImageLoad && !TextUtils.isEmpty(mSourceImageUrl)) {
            if (mSourceImageUrl != null) {
                Glide
                        .with(getContext())
                        .load(mSourceImageUrl)
                        .centerCrop()
                        .error(R.drawable.shape_rounded_rectangle_gray)
                        .placeholder(R.drawable.shape_rounded_rectangle_gray)
                        .into(this);
            } else {
                setImageResource(R.drawable.shape_rounded_rectangle_gray);
            }
        }
    }
}
