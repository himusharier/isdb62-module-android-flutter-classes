package com.himusharier.studentcrudapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.himusharier.studentcrudapp.R;
import com.himusharier.studentcrudapp.activity.AddStudentActivity;
import com.himusharier.studentcrudapp.model.Student;
import com.himusharier.studentcrudapp.service.ApiService;
import com.himusharier.studentcrudapp.util.ApiClient;

import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context context;
    private List<Student> studentList;
    private ApiService apiService;
    private DateTimeFormatter formatter;

    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
        this.apiService = ApiClient.getApiService();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);

        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.nameText.setText(student.getName());
        holder.classText.setText(student.getClazz());
        holder.dobText.setText(student.getDob());
//        holder.dobText.setText(student.getDob().format(formatter));
//        holder.ageText.setText(student.getAge());
        holder.ageText.setText(String.valueOf(student.getAge()));
        holder.addressText.setText(student.getAddress());

        holder.updateButton.setOnClickListener(v -> {
            Log.d("Update", "Update clicked for " + student.getName());
            Intent intent = new Intent(context, AddStudentActivity.class);
            intent.putExtra("student", new Gson().toJson(student));
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            Log.d("Delete", "Delete clicked for " + student.getName());
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete " + student.getName() + "?")
                    .setPositiveButton("Yes",
                            (dialog, which) -> apiService.deleteStudent(student.getId())
                                    .enqueue(new Callback<>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                int adapterPosition = holder.getAdapterPosition();
                                                if (adapterPosition != RecyclerView.NO_POSITION) {
                                                    studentList.remove(adapterPosition);
                                                    notifyItemRemoved(adapterPosition);
                                                    notifyItemRangeChanged(adapterPosition, studentList.size());
                                                    Toast.makeText(context, "Deleted successfully",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                            Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, classText, dobText, ageText, addressText;
        Button updateButton, deleteButton;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            classText = itemView.findViewById(R.id.classText);
            dobText = itemView.findViewById(R.id.dobText);
            ageText = itemView.findViewById(R.id.ageText);
            addressText = itemView.findViewById(R.id.addressText);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
