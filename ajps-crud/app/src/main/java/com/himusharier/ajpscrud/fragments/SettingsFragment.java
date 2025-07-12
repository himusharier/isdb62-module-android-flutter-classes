package com.himusharier.ajpscrud.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.himusharier.ajpscrud.LoginActivity;
import com.himusharier.ajpscrud.R;
import com.himusharier.ajpscrud.services.AuthService;

public class SettingsFragment extends Fragment {

    private AuthService authService;
    private Button btnChangePassword, btnNotifications, btnDeleteAccount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = new AuthService(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnNotifications = view.findViewById(R.id.btnNotifications);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);

        // Set up click listeners
        btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Change password feature coming soon", Toast.LENGTH_SHORT).show();
        });

        btnNotifications.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Notification settings coming soon", Toast.LENGTH_SHORT).show();
        });

        btnDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete", (dialog, which) -> deleteAccount())
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void deleteAccount() {
        // Clear all user data
        authService.logout();

        // Clear SharedPreferences
        android.content.SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

        // Redirect to login
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
