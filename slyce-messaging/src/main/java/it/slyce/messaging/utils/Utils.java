package it.slyce.messaging.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by matthewpage on 6/27/16.
 */
public class Utils {
    public static float getWidthToHeightRatio(String url, Context context) {
        if (context == null)
            System.err.println("WARNING: You should call MediaMessage.setContext(Context) before you pass the object to the fragment.");
        try {
            final Bitmap image = Picasso.with(context).load(url).get();
            float width = image.getWidth();
            float height = image.getHeight();
            return width / height;
        } catch (Exception e) {
            System.err.println("WARNING: Something went wrong. Either the context is null or the image does not exsit.");
            e.printStackTrace();
            return 1f; // Try to make the program not crash anyway.
        }
    }

    public static String getTimestamp(long then) {
        long now = System.currentTimeMillis();

        // convert to seconds
        long nowSeconds = now / 1000;
        long thenSeconds = then / 1000;

        int minutesAgo = ((int) (nowSeconds - thenSeconds)) / (60);
        if (minutesAgo < 1)
            return "Just now";
        else if (minutesAgo == 1)
            return "1 minute ago";
        else if (minutesAgo < 60)
            return minutesAgo + " minutes ago";

        // convert to minutes
        long nowMinutes = nowSeconds / 60;
        long thenMinutes = thenSeconds / 60;

        int hoursAgo = (int) (nowMinutes - thenMinutes) / 60;
        int thenDayOfMonth = getDayOfMonth(then);
        int nowDayOfMonth = getDayOfMonth(now);
        if (hoursAgo < 7 && thenDayOfMonth == nowDayOfMonth) {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            return simpleDateFormat.format(date);
        }

        // convert to hours
        long nowHours = nowMinutes / 60;
        long thenHours = thenMinutes / 60;

        int daysAgo = (int) (nowHours - thenHours) / 24;
        if (daysAgo == 1) {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            return "Yesterday " + simpleDateFormat.format(date);
        } else if (daysAgo < 7) {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE h:mm a");
            return simpleDateFormat.format(date);
        }

        int thenYear = getYear(then);
        int nowYear = getYear(now);
        if (thenYear == nowYear) {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, h:mm a");
            return simpleDateFormat.format(date);
        } else {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, h:mm a");
            return simpleDateFormat.format(date);
        }
    }

    private static int getDayOfMonth(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormate = new SimpleDateFormat("d");
        return Integer.parseInt(simpleDateFormate.format(date));
    }

    private static int getYear(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormate = new SimpleDateFormat("yyyy");
        return Integer.parseInt(simpleDateFormate.format(date));
    }

    public static boolean dateNeedsUpdated(long time, String date) {
        return date.equals(getTimestamp(time));
    }
}
