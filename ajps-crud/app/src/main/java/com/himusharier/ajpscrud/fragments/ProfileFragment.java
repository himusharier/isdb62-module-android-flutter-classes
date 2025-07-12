package com.himusharier.ajpscrud.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.himusharier.ajpscrud.R;
import com.himusharier.ajpscrud.services.AuthService;

public class ProfileFragment extends Fragment {

    private AuthService authService;
    private TextView tvUserName, tvUserEmail, tvUserRole, tvUserId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = new AuthService(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserRole = view.findViewById(R.id.tvUserRole);
        tvUserId = view.findViewById(R.id.tvUserId);

        // Load user data
        loadUserData();
    }

    private void loadUserData() {
        String email = authService.getUserEmail();
        String role = authService.getUserRole();
        int userId = authService.getUserId();

        // Extract name from email (before @ symbol)
        String name = email.contains("@") ? email.split("@")[0] : email;

        tvUserName.setText(name);
        tvUserEmail.setText(email);
        tvUserRole.setText(role.toUpperCase());
        tvUserId.setText(String.valueOf(userId));
    }
}
