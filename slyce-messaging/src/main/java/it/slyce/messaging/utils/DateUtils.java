package it.slyce.messaging.utils;

import android.content.Context;
import android.content.res.Resources;
import it.slyce.messaging.R;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by matthewpage on 6/27/16.
 */
public class DateUtils {

    public static String getTimestamp(Context context, long then) {
        long now = System.currentTimeMillis();

        // convert to seconds
        long nowSeconds = now / 1000;
        long thenSeconds = then / 1000;

        int minutesAgo = ((int) (nowSeconds - thenSeconds)) / (60);
        if (minutesAgo < 1)
            return context.getString(R.string.date_now);
        else if (minutesAgo == 1)
            return context.getString(R.string.date_a_minute_ago);
        else if (minutesAgo < 60)
            return minutesAgo + " " + context.getString(R.string.date_minutes_ago);

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
            String yesterdayString = context.getString(R.string.date_yesterday);
            return yesterdayString + " " + simpleDateFormat.format(date);
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

    public static boolean dateNeedsUpdated(Context context, long time, String date) {
        return date == null || date.equals(getTimestamp(context, time));
    }
}
