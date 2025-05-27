package com.himusharier.studentcrudapp.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.himusharier.studentcrudapp.R;
import com.himusharier.studentcrudapp.model.Student;
import com.himusharier.studentcrudapp.service.ApiService;
import com.himusharier.studentcrudapp.util.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewStudentActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeListActivity";
    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private List<Student>studentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void fetchEmployees() {
        ApiService apiService = ApiClient.getApiService();
        Call<List<Student>> call = apiService.getAllStudent();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    List<Student> students = response.body();
                    assert students != null;
                    for (Student student : students) {
                        Log.d(TAG, "ID: " + student.getId() + ", Name: "
                                + student.getName() + ", Designation: " + student.getDesignation());
                    }
                    DiffUtil.DiffResult result = DiffUtil.calculateDiff(new EmployeeDiffCallback(employeeList, employees));
                    employeeList.clear();
                    employeeList.addAll(employees);
                    // The below code does change the whole content of the RecyclerView which is inefficient
                    // employeeAdapter.notifyDataSetChanged();
                    // Rather use this one
                    result.dispatchUpdatesTo(employeeAdapter);
                } else {
                    Log.e(TAG, "API Response Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Employee>> call, @NonNull Throwable t) {
                Log.e(TAG, "API Call Failed: " + t.getMessage());
            }
        });
    }

}