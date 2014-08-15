package com.vee.healthplus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {

    public static boolean getBooleanPref(Context context, String name, boolean def) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getBoolean(name, def);
    }

    public static void setBooleanPref(Context context, String name, boolean value) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Editor ed = prefs.edit();
        ed.putBoolean(name, value);
        ed.commit();
    }

    public static int getIntPref(Context context, String name, int def) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getInt(name, def);
    }

    public static void setIntPref(Context context, String name, int value) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Editor ed = prefs.edit();
        ed.putInt(name, value);
        ed.commit();
    }

    public static String getStringPref(Context context, String name, String def) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getString(name, def);
    }

    public static void setStringPref(Context context, String name, String value) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Editor ed = prefs.edit();
        ed.putString(name, value);
        ed.commit();
    }

    public static long getLongPref(Context context, String name, long def) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return prefs.getLong(name, def);
    }

    public static void setLongPref(Context context, String name, Long value) {
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        Editor ed = prefs.edit();
        ed.putLong(name, value);
        ed.commit();
    }
}
