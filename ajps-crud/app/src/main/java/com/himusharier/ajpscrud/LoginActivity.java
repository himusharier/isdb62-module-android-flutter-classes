package com.himusharier.ajpscrud;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.himusharier.ajpscrud.databinding.ActivityLoginBinding;
import com.himusharier.ajpscrud.models.AuthRegisterLoginRequest;
import com.himusharier.ajpscrud.models.AuthResponse;
import com.himusharier.ajpscrud.services.AuthService;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authService = new AuthService(this);

        // Check if user is already authenticated
        checkAuthStatus();

        binding.btnLogin.setOnClickListener(v -> loginUser());
        binding.tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void checkAuthStatus() {
        if (authService.isAuthenticated()) {
            authService.validateToken(new AuthService.AuthCallback() {
                @Override
                public void onSuccess(AuthResponse response) {
                    runOnUiThread(() -> navigateBasedOnRole());
                }

                @Override
                public void onError(AuthResponse response) {
                    // Token invalid, stay on login page
                }
            });
        }
    }

    private void loginUser() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button during login
        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Logging in...");

        AuthRegisterLoginRequest request = new AuthRegisterLoginRequest(email, password);

        authService.login(request, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                runOnUiThread(() -> {
                    if (response.getAccessToken() != null) {
                        authService.setToken(response.getAccessToken());
                    }
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    navigateBasedOnRole();
                });
            }

            @Override
            public void onError(AuthResponse response) {
                runOnUiThread(() -> {
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setText("Login");
                    Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void navigateBasedOnRole() {
        // Always redirect to ProfileActivity after login
        Toast.makeText(this, "Navigating to Profile...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
