package com.gabri.gpschat.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gabri on 20/02/2017.
 */

public class Utils {

    public static String getFromPref(String key, Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(Constants.KEY_COOKIES, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void setToPrefString(String key, String value, Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(Constants.KEY_COOKIES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStringFromDate(String date)
    {
        long time = Long.parseLong(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date dd = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(dd);
    }
}
