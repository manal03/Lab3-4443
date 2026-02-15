package com.example.lab3_4443;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsStorage {

    private static final String PREFS_NAME = "LAB3_PREFS";
    private static final String KEY_COUNT = "count";

    public static void saveContact(Context context, Contact c) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int count = sp.getInt(KEY_COUNT, 0);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name_" + count, c.name);
        editor.putString("phone_" + count, c.phone);
        editor.putString("date_" + count, c.dateAdded);
        editor.putString("desc_" + count, c.description);
        editor.putInt(KEY_COUNT, count + 1);
        editor.apply();
    }
}
