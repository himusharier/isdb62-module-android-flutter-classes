package com.example.demoapp2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextDob;
//    private TextInputLayout dateLayout;
    private EditText textName, textEmail, textDesignation, numberAge, multilineAddress, decimalSalary;
    private Button btnSave;
//    private ApiService apiService;

    @SuppressLint({"WrongViewCast", "CutPasteId", "DefaultLocale"})
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

        editTextDob = findViewById(R.id.editTextDob);
//        dateLayout = findViewById(R.id.)
        textName = findViewById(R.id.editTextName);
        textEmail = findViewById(R.id.editTextEmail);
        textDesignation = findViewById(R.id.editTextDesignation);
        numberAge = findViewById(R.id.editTextAge);
        multilineAddress = findViewById(R.id.editTextAddress);
        decimalSalary = findViewById(R.id.editTextSalary);
        btnSave = findViewById(R.id.buttonSave);

        editTextDob.setOnClickListener(v -> showDatePicker());

        /*EditText editTextDate = findViewById(R.id.editTextDob);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Format the selected date as YYYY-MM-DD
                                String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                                editTextDate.setText(selectedDate);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });*/

    }

    private  void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(this,
                (view, year1, month1, day1) -> {
                    String dob = String.format(Locale.US, "%04d-%02d-%02d", year1, month1, day1);
                },
                year, month, day);
        picker.show();
    }
}