package it.slyce.messaging.utils;

import android.content.Context;

public class MediaUtils {
    public static float getWidthToHeightRatio(String url, Context context) {
        if (context == null)
            System.err.println("WARNING: You should call MediaMessage.setContext(Context) before you pass the object to the fragment.");
        try {
            // TODO actually do this
            // final Bitmap image = Glide.with(context).load(url).get();
            // float width = image.getWidth();
            // float height = image.getHeight();
            // return width / height;
            return 1f;
        } catch (Exception e) {
            // System.err.println("WARNING: Something went wrong. Either the context is null or the image does not exist.");
            // e.printStackTrace();
            return 1f; // Try to make the program not crash anyway.
        }
    }
}
