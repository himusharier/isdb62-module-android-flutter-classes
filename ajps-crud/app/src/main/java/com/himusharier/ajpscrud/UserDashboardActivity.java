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
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationView;
import com.himusharier.ajpscrud.databinding.ActivityUserDashboardBinding;
import com.himusharier.ajpscrud.fragments.DashboardFragment;
import com.himusharier.ajpscrud.fragments.EditProfileFragment;
import com.himusharier.ajpscrud.fragments.OnlineSubmissionFragment;
import com.himusharier.ajpscrud.fragments.ProfileFragment;
import com.himusharier.ajpscrud.fragments.SettingsFragment;
import com.himusharier.ajpscrud.services.AuthService;

public class UserDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityUserDashboardBinding binding;
    private AuthService authService;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private int selectedIndex = 0;

    private final String[] titles = {
        "Dashboard", "Profile", "Edit Profile", "Online Submission", "Settings"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
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

        // Load initial fragment
        loadFragment(new DashboardFragment());
        setTitle(titles[selectedIndex]);
    }

    private void setupNavigationHeader() {
        // Get header view and set user info
        TextView emailTextView = binding.navView.getHeaderView(0).findViewById(R.id.nav_header_email);
        TextView roleTextView = binding.navView.getHeaderView(0).findViewById(R.id.nav_header_role);

        if (emailTextView != null) {
            emailTextView.setText(authService.getUserEmail());
        }
        if (roleTextView != null) {
            roleTextView.setText("Role: " + authService.getUserRole());
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_dashboard) {
            fragment = new DashboardFragment();
            selectedIndex = 0;
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
            selectedIndex = 1;
        } else if (id == R.id.nav_edit_profile) {
            fragment = new EditProfileFragment();
            selectedIndex = 2;
        } else if (id == R.id.nav_online_submission) {
            fragment = new OnlineSubmissionFragment();
            selectedIndex = 3;
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            selectedIndex = 4;
        } else if (id == R.id.nav_logout) {
            logout();
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        if (fragment != null) {
            loadFragment(fragment);
            setTitle(titles[selectedIndex]);
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        authService.logout();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(UserDashboardActivity.this, LoginActivity.class);
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
