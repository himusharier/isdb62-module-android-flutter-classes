package com.himusharier.ajpscrud.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.himusharier.ajpscrud.R;
import com.himusharier.ajpscrud.services.AuthService;

public class OnlineSubmissionFragment extends Fragment {

    private AuthService authService;
    private Button btnSubmitManuscript, btnViewSubmissions, btnSubmissionGuidelines;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = new AuthService(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online_submission, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        btnSubmitManuscript = view.findViewById(R.id.btnSubmitManuscript);
        btnViewSubmissions = view.findViewById(R.id.btnViewSubmissions);
        btnSubmissionGuidelines = view.findViewById(R.id.btnSubmissionGuidelines);

        // Set up click listeners
        btnSubmitManuscript.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Submit Manuscript feature coming soon", Toast.LENGTH_SHORT).show();
        });

        btnViewSubmissions.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "View Submissions feature coming soon", Toast.LENGTH_SHORT).show();
        });

        btnSubmissionGuidelines.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Submission Guidelines feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }
}
