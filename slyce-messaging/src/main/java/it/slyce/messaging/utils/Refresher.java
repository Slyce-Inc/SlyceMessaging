package it.slyce.messaging.utils;

/**
 * @Auther Matthew Page
 * @Date 7/13/16
 */
public class Refresher {
    private boolean mIsRefreshing;

    public Refresher(boolean mIsRefreshing) {
        this.mIsRefreshing = mIsRefreshing;
    }

    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    public void setIsRefreshing(boolean mIsRefreshing) {
        this.mIsRefreshing = mIsRefreshing;
    }
}
