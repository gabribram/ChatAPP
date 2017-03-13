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
        String result = pref.getString(key, "");
        if (result == null) return "";
        return result;
    }

    public static void setToPrefString(String key, String value, Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(Constants.KEY_COOKIES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (value == null) value = "";
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

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
