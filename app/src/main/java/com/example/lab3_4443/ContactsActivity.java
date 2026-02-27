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

import android.app.AlertDialog;
import android.widget.Toast;

public class ContactsActivity extends AppCompatActivity {

    ListView listViewContacts;
    ArrayAdapter<String> adapter;
    ArrayList<String> displayList = new ArrayList<>();
    List<Contact> contactObjects = new ArrayList<>();
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
        //Connects adapter to listview
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        //Now knows what data to present
        listViewContacts.setAdapter(adapter);

        // TAP: show details
        listViewContacts.setOnItemClickListener((parent, view, position, id) -> {
            Contact c = contactObjects.get(position);

            new AlertDialog.Builder(ContactsActivity.this)
                    .setTitle("Contact Details")
                    .setMessage(
                            "Name: " + c.getName() +
                                    "\nPhone: " + c.getPhone() +
                                    "\nDate Added: " + c.getDateAdded() +
                                    "\nDescription: " + c.getDescription()
                    )
                    .setPositiveButton("OK", null)
                    .show();
        });

        // LONG PRESS: delete
        listViewContacts.setOnItemLongClickListener((parent, view, position, id) -> {
            Contact c = contactObjects.get(position);

            new AlertDialog.Builder(ContactsActivity.this)
                    .setTitle("Delete Contact")
                    .setMessage("Delete " + c.getName() + " (" + c.getPhone() + ")?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        if (useSqliteCheck.isChecked()) {
                            dbHelper.deleteByPhone(c.getPhone());
                        } else {
                            SharedPrefsStorage.deleteByPhone(this, c.getPhone());
                        }

                        loadContacts(); // refresh list
                        Toast.makeText(this, "Deleted.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        });

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
        contactObjects.clear();
try {
    if (useSqliteCheck.isChecked()) {
        contactObjects = dbHelper.getAllContacts();
    } else {
        contactObjects = SharedPrefsStorage.loadContacts(this);
    }
}catch (Exception e) {
    Toast.makeText(this, "Load failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    contactObjects = new ArrayList<>();
}

        for (Contact c : contactObjects) {
            displayList.add(c.getName() + " | " + c.getPhone());
        }
        if (contactObjects == null || contactObjects.isEmpty()) {
            displayList.add("(No contacts saved yet)");
        }
        adapter.notifyDataSetChanged();
    }
}