package com.example.lab3_4443;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import java.util.Calendar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button submitBtn;
    EditText name, phoneNumber, date, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        submitBtn = findViewById(R.id.submitBtnId);
        name = findViewById(R.id.nameId);
        phoneNumber = findViewById(R.id.phoneNumberId);
        date = findViewById(R.id.dateId);
        description = findViewById(R.id.descriptionId);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateText = date.getText().toString().trim();
                String nameText = name.getText().toString().trim();
                String phoneNumberText = phoneNumber.getText().toString().trim();
                String descriptionText = description.getText().toString().trim();

                if(nameText.isEmpty() || phoneNumberText.isEmpty() || dateText.isEmpty() || descriptionText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Unsuccessful - make sure all values are added.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Successfully added a contact.", Toast.LENGTH_SHORT).show();
                }
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
}