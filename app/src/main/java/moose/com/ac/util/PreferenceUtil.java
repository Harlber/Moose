package moose.com.ac.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Farble on 2015/8/17 23.
 */
public class PreferenceUtil {
    public static SharedPreferences preferences = null;

    public PreferenceUtil(Context context) {
        preferences = context.getSharedPreferences("UserSetting", Context.MODE_PRIVATE);
    }

    public static void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setIntValue(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static void setLongValue(String key, Long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getString(String key, String defaultValue) {
        if (preferences == null) {
            return "";
        }
        return preferences.getString(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }
    public static Long getLong(String key, Long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public static void clearData(Context context) {
        preferences.edit().clear().commit();
    }

    public static boolean contains(String key) {
        return preferences.contains(key);
    }

}