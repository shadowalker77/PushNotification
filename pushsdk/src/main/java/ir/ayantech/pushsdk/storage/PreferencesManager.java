package ir.ayantech.pushsdk.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 10/16/2017.
 */

public class PreferencesManager {
    private static SharedPreferences preferences;

    public static void initialize(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void saveToSharedPreferences(String fieldName, String value) {
        preferences.edit().putString(fieldName, value).apply();
    }

    public static void saveToSharedPreferences(String fieldName, boolean value) {
        preferences.edit().putBoolean(fieldName, value).apply();
    }

    public static void saveToSharedPreferences(String fieldName, Long value) {
        preferences.edit().putLong(fieldName, value).apply();
    }

    public static String readStringFromSharedPreferences(String field) {
        return preferences.getString(field, "");
    }

    public static boolean readBooleanFromSharedPreferences(String field) {
        return preferences.getBoolean(field, false);
    }

    public static Long readLongFromSharedPreferences(String field) {
        return preferences.getLong(field, 0L);
    }
}
