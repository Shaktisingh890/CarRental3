package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "AppPrefs";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String IS_LOGGED_IN_KEY = "is_logged_in"; // Key for login status

    // Save the access token, refresh token, and login status in SharedPreferences
    public static void saveTokens(Context context, String accessToken, String refreshToken, boolean isLoggedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN_KEY, accessToken);
        editor.putString(REFRESH_TOKEN_KEY, refreshToken);
        editor.putBoolean(IS_LOGGED_IN_KEY, isLoggedIn); // Save login status
        editor.apply();
    }

    // Retrieve the access token
    public static String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null); // Default to null if not found
    }

    // Retrieve the refresh token
    public static String getRefreshToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null); // Default to null if not found
    }

    // Retrieve the login status
    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGGED_IN_KEY, false); // Default to false if not found
    }

    // Clear the stored tokens and login status (for logout)
    public static void clearTokens(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(ACCESS_TOKEN_KEY);
        editor.remove(REFRESH_TOKEN_KEY);
        editor.remove(IS_LOGGED_IN_KEY); // Remove the login status as well
        editor.apply();
    }
}
