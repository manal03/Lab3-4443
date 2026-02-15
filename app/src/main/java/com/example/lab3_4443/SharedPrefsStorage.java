package com.example.lab3_4443;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

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

    public static List<Contact> loadContacts(Context context) {

        List<Contact> contacts = new ArrayList<>();

        SharedPreferences sp =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        int count = sp.getInt(KEY_COUNT, 0);

        for (int i = 0; i < count; i++) {

            String name = sp.getString("name_" + i, "");
            String phone = sp.getString("phone_" + i, "");
            String date = sp.getString("date_" + i, "");
            String desc = sp.getString("desc_" + i, "");

            if (!name.isEmpty() && !phone.isEmpty()) {
                contacts.add(new Contact(name, phone, date, desc));
            }
        }

        return contacts;
    }
}
