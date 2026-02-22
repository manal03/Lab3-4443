package com.example.lab3_4443;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    ListView listViewContacts;
    ArrayAdapter<String> adapter;
    ArrayList<String> displayList = new ArrayList<>();
    DBHelper dbHelper;
    CheckBox useSqliteCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Button addBtn = findViewById(R.id.addBtn);
        useSqliteCheck = findViewById(R.id.useSqliteCheckContacts);

        listViewContacts = findViewById(R.id.listViewContacts);
        dbHelper = new DBHelper(this);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );

        listViewContacts.setAdapter(adapter);

        loadContacts();
        useSqliteCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            loadContacts();
        });

        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
            intent.putExtra("FROM_ADD_BUTTON", true);
            startActivity(intent);
            finish();
        });
    }

    private void loadContacts() {
        displayList.clear();

        List<Contact> contacts;

        if (useSqliteCheck.isChecked()) {
            contacts = dbHelper.getAllContacts();
        } else {
            contacts = SharedPrefsStorage.loadContacts(this);
        }
        for (Contact c : contacts) {
            displayList.add(c.getName() + " | " + c.getPhone());
        }

        adapter.notifyDataSetChanged();
    }
}