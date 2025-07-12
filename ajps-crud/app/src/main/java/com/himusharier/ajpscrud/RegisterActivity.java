package com.himusharier.ajpscrud;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.himusharier.ajpscrud.databinding.ActivityRegisterBinding;
import com.himusharier.ajpscrud.models.AuthRegisterLoginRequest;
import com.himusharier.ajpscrud.models.AuthResponse;
import com.himusharier.ajpscrud.services.AuthService;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authService = new AuthService(this);

        binding.btnRegister.setOnClickListener(v -> registerUser());
        binding.tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button during registration
        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setText("Registering...");

        AuthRegisterLoginRequest request = new AuthRegisterLoginRequest(email, password, "USER");

        authService.register(request, new AuthService.AuthCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "Registration successful! You can now log in.", Toast.LENGTH_LONG).show();

                    // Navigate to login screen after a delay
                    binding.btnRegister.postDelayed(() -> {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }, 2000);
                });
            }

            @Override
            public void onError(AuthResponse response) {
                runOnUiThread(() -> {
                    binding.btnRegister.setEnabled(true);
                    binding.btnRegister.setText("Register");
                    Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
