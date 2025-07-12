package com.himusharier.ajpscrud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.himusharier.ajpscrud.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Set up toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        binding.btnDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
        binding.btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(this, "Change password feature coming soon", Toast.LENGTH_SHORT).show();
        });
        binding.btnNotifications.setOnClickListener(v -> {
            Toast.makeText(this, "Notification settings coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void deleteAccount() {
        // Clear all user data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();

        // Redirect to login
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
