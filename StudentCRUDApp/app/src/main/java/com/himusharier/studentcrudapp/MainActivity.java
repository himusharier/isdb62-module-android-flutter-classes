package com.himusharier.studentcrudapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.himusharier.studentcrudapp.activity.AddStudentActivity;
import com.himusharier.studentcrudapp.activity.ViewStudentActivity;

public class MainActivity extends AppCompatActivity {

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

        Button btnAddStudent = findViewById(R.id.addStudent);
        Button btnViewStudent = findViewById(R.id.viewStudent);

        btnAddStudent.setOnClickListener(v -> navigateToAddStudentPage());
        btnViewStudent.setOnClickListener(v -> navigateToViewStudentPage());

    }

    private void navigateToAddStudentPage() {
        Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
        startActivity(intent);
    }

    private void navigateToViewStudentPage() {
        Intent intent = new Intent(MainActivity.this, ViewStudentActivity.class);
        startActivity(intent);
    }

}