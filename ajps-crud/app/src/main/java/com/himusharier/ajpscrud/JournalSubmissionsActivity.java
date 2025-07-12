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
import com.himusharier.ajpscrud.databinding.ActivityJournalSubmissionsBinding;
import com.himusharier.ajpscrud.services.AuthService;

public class JournalSubmissionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityJournalSubmissionsBinding binding;
    private AuthService authService;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJournalSubmissionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authService = new AuthService(this);

        // Find views
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        setTitle("Journal Submissions");
    }

    private void setupNavigationHeader() {
        TextView emailTextView = binding.navView.getHeaderView(0).findViewById(R.id.nav_header_email);
        TextView roleTextView = binding.navView.getHeaderView(0).findViewById(R.id.nav_header_role);

        if (emailTextView != null) {
            emailTextView.setText(authService.getUserEmail());
        }
        if (roleTextView != null) {
            roleTextView.setText("Role: " + authService.getUserRole().toUpperCase());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_editor_submissions) {
            Toast.makeText(this, "All Submissions", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_editor_reviews) {
            Toast.makeText(this, "Review Management", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_editor_publications) {
            Toast.makeText(this, "Publications", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_editor_settings) {
            Toast.makeText(this, "Editor Settings", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        authService.logout();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(JournalSubmissionsActivity.this, LoginActivity.class);
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
}
