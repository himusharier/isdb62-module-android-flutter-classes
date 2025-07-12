package com.himusharier.ajpscrud;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.himusharier.ajpscrud.services.AuthService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthService authService = new AuthService(this);
        Intent intent;

        if (authService.isAuthenticated()) {
            // If the user is authenticated, go to the Profile page
            intent = new Intent(this, ProfileActivity.class);
        } else {
            // Otherwise, go to the Login page
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
