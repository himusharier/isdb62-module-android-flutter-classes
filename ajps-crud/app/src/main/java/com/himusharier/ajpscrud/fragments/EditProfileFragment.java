package com.himusharier.ajpscrud.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.himusharier.ajpscrud.R;
import com.himusharier.ajpscrud.services.AuthService;

public class EditProfileFragment extends Fragment {

    private AuthService authService;
    private EditText etName, etEmail, etPhone, etBio;
    private Button btnSave;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = new AuthService(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etBio = view.findViewById(R.id.etBio);
        btnSave = view.findViewById(R.id.btnSave);

        // Load current user data
        loadUserData();

        // Set up save button
        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadUserData() {
        String email = authService.getUserEmail();
        String name = email.contains("@") ? email.split("@")[0] : email;

        etName.setText(name);
        etEmail.setText(email);

        // Load additional profile data from SharedPreferences if available
        android.content.SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE);
        etPhone.setText(prefs.getString("phone", ""));
        etBio.setText(prefs.getString("bio", ""));
    }

    private void saveProfile() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext(), "Name and email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to SharedPreferences (in a real app, this would be an API call)
        android.content.SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("bio", bio);
        editor.apply();

        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
}
