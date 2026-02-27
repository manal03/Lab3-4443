package com.example.lab3_4443;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.util.Patterns;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.CheckBox;



public class MainActivity extends AppCompatActivity {
    //Variable Declarations
    Button submitBtn;
    EditText name, phoneNumber, date, description;
    //Checkbox that stores checked preference
    CheckBox useSqliteCheck;
    //Handles SQLite operations
    DBHelper dbHelper;
    //A local list
    ArrayList<Contact> contactObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        useSqliteCheck = findViewById(R.id.useSqliteCheck);
        dbHelper = new DBHelper(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Linking UI elements to java
        submitBtn = findViewById(R.id.submitBtnId);
        name = findViewById(R.id.nameId);
        phoneNumber = findViewById(R.id.phoneNumberId);
        date = findViewById(R.id.dateId);
        description = findViewById(R.id.descriptionId);

        //Initialize database helper so we can call this to add, or remove data (calls class)
        dbHelper = new DBHelper(this);
        //Checks if user came from a certain screen
        boolean fromAddButton = getIntent().getBooleanExtra("FROM_ADD_BUTTON", false);
        List<Contact> existingContacts = dbHelper.getAllContacts();
        //If contacts already exist and we didnt click "add contacts" button, redirect to this page immediately
        if (!existingContacts.isEmpty() && !fromAddButton) {
            Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
            startActivity(intent);
            finish();
        }

        //
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateText = date.getText().toString().trim();
                String nameText = name.getText().toString().trim();
                String phoneNumberText = phoneNumber.getText().toString().trim();
                String descriptionText = description.getText().toString().trim();



                if (nameText.isEmpty() || phoneNumberText.isEmpty() || dateText.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Unsuccessful - required fields missing (Name, Phone, Date).",
                            Toast.LENGTH_SHORT).show();
                    return;

                }
//Validation
                name.setError(null);
                phoneNumber.setError(null);
                date.setError(null);

// Name sanity
                if (nameText.length() < 2) {
                    name.setError("Name is too short");
                    name.requestFocus();
                    return;
                }

// Phone format check
                String phoneNormalized = normalizePhone(phoneNumberText);
                if (!isValidPhone(phoneNumberText)) {
                    phoneNumber.setError("Enter a valid phone number");
                    phoneNumber.requestFocus();
                    return;
                }


// Edge Case: Prevent duplicate contacts by phone
                if (phoneExistsAlready(phoneNormalized)) {
                    phoneNumber.setError("A contact with this phone already exists");
                    phoneNumber.requestFocus();
                    return;
                }
                // Create model
                Contact c = new Contact(nameText, phoneNumberText, dateText, descriptionText);

                // Choose storage method at runtime
                try {
                    if (useSqliteCheck.isChecked()) {
                        long rowId = dbHelper.insertContact(c);
                        if (rowId == -1) {
                            Toast.makeText(MainActivity.this, "SQLite save failed.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(MainActivity.this, "Saved to SQLite!", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPrefsStorage.saveContact(MainActivity.this, c);
                        Toast.makeText(MainActivity.this, "Saved to SharedPreferences!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Clear the form
                name.setText("");
                phoneNumber.setText("");
                date.setText("");
                description.setText("");

                Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(intent);
                finish();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {

                            // Month is 0-based so add +1
                            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

                            date.setText(selectedDate);
                        },
                        year, month, day
                );

                datePickerDialog.show();
            }
        });
    }
    // Validation helpers
    private String normalizePhone(String raw) {
        if (raw == null) return "";
        // keep digits and leading +
        String cleaned = raw.trim().replaceAll("[^0-9+]", "");
        // allow only one leading +
        if (cleaned.startsWith("+")) {
            cleaned = "+" + cleaned.substring(1).replaceAll("\\+", "");
        } else {
            cleaned = cleaned.replaceAll("\\+", "");
        }
        return cleaned;
    }

    private boolean isValidPhone(String raw) {
        if (raw == null) return false;
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) return false;

        // Built-in Android pattern
        if (!Patterns.PHONE.matcher(trimmed).matches()) return false;

        // Digit length sanity check (7–15 digits)
        String digitsOnly = normalizePhone(trimmed).replace("+", "");
        return digitsOnly.length() >= 7 && digitsOnly.length() <= 15;
    }


    private boolean phoneExistsAlready(String phoneNormalized) {
        if (phoneNormalized == null || phoneNormalized.trim().isEmpty()) return false;

        if (useSqliteCheck.isChecked()) {
            List<Contact> all = dbHelper.getAllContacts();
            for (Contact c : all) {
                if (c != null && phoneNormalized.equals(normalizePhone(c.getPhone()))) return true;
            }
            return false;
        } else {
            List<Contact> all = SharedPrefsStorage.loadContacts(this);
            for (Contact c : all) {
                if (c != null && phoneNormalized.equals(normalizePhone(c.getPhone()))) return true;
            }
            return false;
        }
    }
}