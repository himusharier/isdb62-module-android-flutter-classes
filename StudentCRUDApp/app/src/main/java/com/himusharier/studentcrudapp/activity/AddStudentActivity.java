package com.himusharier.studentcrudapp.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.himusharier.studentcrudapp.R;
import com.himusharier.studentcrudapp.model.Student;
import com.himusharier.studentcrudapp.service.ApiService;
import com.himusharier.studentcrudapp.util.ApiClient;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class AddStudentActivity extends AppCompatActivity {

    private TextInputEditText editTextDob;
    private TextInputLayout dateLayout;
    private EditText editTextName, editTextClass, editNumberAge, multilineAddress;
    private Button btnSave;
    private ApiService apiService = ApiClient.getApiService();
    private boolean isEditMode = false;
    private int studentId = -1;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enable the Up button (back arrow) in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editTextDob = findViewById(R.id.editTextDob);
        dateLayout = findViewById(R.id.dateLayout);
        editTextName = findViewById(R.id.editTextName);
        editTextClass = findViewById(R.id.editTextClass);
        editNumberAge = findViewById(R.id.editNumberAge);
        multilineAddress = findViewById(R.id.multilineAddress);
        btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        if (getIntent().hasExtra("student")) {
            Student student = new Gson()
                    .fromJson(intent.getStringExtra("student"), Student.class);
            assert student != null;
            studentId = student.getId();

            editTextName.setText(student.getName());
            editTextClass.setText(student.getClazz());
//            editNumberAge.setText(student.getAge());
            editNumberAge.setText(String.valueOf(student.getAge()));
            multilineAddress.setText(student.getAddress());
            editTextDob.setText(student.getDob());
//            editTextDob.setText(student.getDob().format(formatter));

            btnSave.setText(R.string.updateButton);
            isEditMode = true;
        }

        btnSave.setOnClickListener(v -> saveOrUpdateStudent());
        // dateLayout.setEndIconOnClickListener(v -> showDatePicker());
        dateLayout.setEndIconOnClickListener(v -> showMaterialDatePicker());

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Not so good with year picking
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(
                this,
                (view, year1, month1, day1) -> {
                    String dob = String.format(Locale.US, "%04d-%02d-%02d", year1, month1, day1);
                    editTextDob.setText(dob);
                },
                year,
                month,
                day
        );
        picker.show();
    }

    // More moder date picker for material
    private void showMaterialDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date of Birth")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.show(getSupportFragmentManager(), "DOB_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Convert selected timestamp to yyyy-MM-dd
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String dob = sdf.format(new Date(selection));
            editTextDob.setText(dob);
        });
    }


    private void saveOrUpdateStudent() {
        String name = editTextName.getText().toString().trim();
        String clazz = editTextClass.getText().toString().trim();
        int age = Integer.parseInt(editNumberAge.getText().toString().trim());
        String address = multilineAddress.getText().toString().trim();
        assert editTextDob.getText() != null;
        String dobString = editTextDob.getText().toString().trim();
//        LocalDate dobString = LocalDate.parse(editTextDob.getText());

        Student student = new Student();
        if (isEditMode) {
            student.setId(studentId);
        }
        student.setName(name);
        student.setClazz(clazz);
        student.setDob(dobString);
        student.setAge(age);
        student.setAddress(address);

        Call<Student> call;
        if (isEditMode) {
            call = apiService.updateStudent(studentId, student);
        } else {
            call = apiService.saveStudent(student);
        }

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Student> call, @NonNull Response<Student> response) {
                if (response.isSuccessful()) {
                    String message;
                    if (isEditMode)
                        message = "Student Updated Successfully!";
                    else
                        message = "Student Saved Successfully!";

                    Toast.makeText(AddStudentActivity.this, message, Toast.LENGTH_SHORT).show();
                    clearForm();
                    Intent intent = new Intent(AddStudentActivity.this, ViewStudentActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddStudentActivity.this, "Failed to save student "
                            + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Student> call, @NonNull Throwable t) {
                Toast.makeText(AddStudentActivity.this, "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        editTextName.setText("");
        editTextClass.setText("");
        editTextDob.setText("");
        editNumberAge.setText("");
        multilineAddress.setText("");
    }


}