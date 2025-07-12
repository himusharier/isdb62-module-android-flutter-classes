package com.himusharier.ajpscrud.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.himusharier.ajpscrud.R;
import com.himusharier.ajpscrud.services.AuthService;

public class DashboardFragment extends Fragment {

    private AuthService authService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = new AuthService(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize dashboard UI components here
        setupDashboard();
    }

    private void setupDashboard() {
        // Load dashboard data based on user role
        String userRole = authService.getUserRole();
        // Implement dashboard logic based on user role
    }
}
