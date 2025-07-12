package com.himusharier.ajpscrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import com.himusharier.ajpscrud.databinding.ActivityProfileBinding;
import com.himusharier.ajpscrud.services.AuthService;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityProfileBinding binding;
    private AuthService authService;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Debug: Add toast to confirm ProfileActivity is starting
        Toast.makeText(this, "ProfileActivity starting...", Toast.LENGTH_SHORT).show();

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authService = new AuthService(this);

        // Find views from the included layout
        toolbar = findViewById(R.id.toolbar);

        // Set up toolbar
        setSupportActionBar(toolbar);

        // Set up drawer
        toggle = new ActionBarDrawerToggle(
            this, binding.drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        // Set up navigation header
        setupNavigationHeader();

        // Load user data in the main content area
        loadUserData();

        // Debug: Add toast to confirm ProfileActivity setup is complete
        Toast.makeText(this, "ProfileActivity setup complete", Toast.LENGTH_SHORT).show();
    }

    private void setupNavigationHeader() {
        // Get header view and set user info
        TextView emailTextView = binding.navView.getHeaderView(0).findViewById(R.id.nav_header_email);
        TextView roleTextView = binding.navView.getHeaderView(0).findViewById(R.id.nav_header_role);

        if (emailTextView != null) {
            emailTextView.setText(authService.getUserEmail());
        }
        if (roleTextView != null) {
            roleTextView.setText("Role: " + authService.getUserRole().toUpperCase());
        }
    }

    private void loadUserData() {
        // Find the TextViews in the main content area
        TextView tvUserName = findViewById(R.id.tvUserName);
        TextView tvUserEmail = findViewById(R.id.tvUserEmail);

        String email = authService.getUserEmail();
        String role = authService.getUserRole();

        // Extract name from email (before @ symbol)
        String name = email.contains("@") ? email.split("@")[0] : email;

        if (tvUserName != null) {
            tvUserName.setText(name);
        }
        if (tvUserEmail != null) {
            tvUserEmail.setText(email);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Already on profile page
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        authService.logout();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupNavigationHeader(); // Refresh data when returning from edit profile
        loadUserData();
    }
}
