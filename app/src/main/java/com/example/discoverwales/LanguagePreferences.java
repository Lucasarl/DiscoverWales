package com.example.discoverwales;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.mlkit.nl.translate.TranslateLanguage;

public class LanguagePreferences {
    private static final String PREFS_NAME = "language_prefs";
    private static final String KEY_LANGUAGE = "selected_language";

    public static void saveLanguage(Context context, String languageCode) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LANGUAGE, languageCode);
        editor.apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_LANGUAGE, TranslateLanguage.ENGLISH); // Default to English
    }
}
