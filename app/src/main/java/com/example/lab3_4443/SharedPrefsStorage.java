package com.example.lab3_4443;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefsStorage {
//This class stores everything in "stored preference"
    //Name of shared preference file
    private static final String PREFS_NAME = "LAB3_PREFS";
    //Stores how many contacts have been stored
    private static final String KEY_COUNT = "count";

    //Static method that stores one contact at a time
    public static void saveContact(Context context, Contact c) {
        //The storage file is opened here
        SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int count = sp.getInt(KEY_COUNT, 0);
        //Editing storage via this
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name_" + count, c.name);
        editor.putString("phone_" + count, c.phone);
        editor.putString("date_" + count, c.dateAdded);
        editor.putString("desc_" + count, c.description);
        editor.putInt(KEY_COUNT, count + 1);
        editor.apply();
    }
    //Loads all of the contacts
    public static List<Contact> loadContacts(Context context) {
        //Empty contacts list
        List<Contact> contacts = new ArrayList<>();
        //Open the shared preference file
        SharedPreferences sp =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //This reads how many contacts we have so we can run a for loop when displaying
        int count = sp.getInt(KEY_COUNT, 0);

        for (int i = 0; i < count; i++) {

            String name = sp.getString("name_" + i, "");
            String phone = sp.getString("phone_" + i, "");
            String date = sp.getString("date_" + i, "");
            String desc = sp.getString("desc_" + i, "");
            //This is just for validation/safety purposes, make sure name and phone number
            //exist before we add the contact
            if (!name.isEmpty() && !phone.isEmpty()) {
                contacts.add(new Contact(name, phone, date, desc));
            }
        }

        return contacts;
    }
    //This deletes a contact
    public static void deleteByPhone(Context context, String phone) {
        List<Contact> all = loadContacts(context);

        // remove first match
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getPhone().equals(phone)) {
                all.remove(i);
                break;
            }
        }

        // clear and rewrite
        SharedPreferences sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        // rebuild with same format as saveContact()
        for (Contact c : all) {
            saveContact(context, c);
        }
    }
}
