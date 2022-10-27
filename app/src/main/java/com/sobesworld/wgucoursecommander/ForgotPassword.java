package com.sobesworld.wgucoursecommander;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText editTextEmail;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = findViewById(R.id.email_forgot);

        Button sendLinkButton = findViewById(R.id.send_link_button);
        sendLinkButton.setOnClickListener(view -> resetPassword());

        Button cancelButton = findViewById(R.id.cancel_button_forgot);
        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
            startActivity(intent);
        });

        progressBar = findViewById(R.id.reset_progress);

        mAuth = FirebaseAuth.getInstance();
    }

    private void resetPassword() {
        String email = editTextEmail.getText().toString().trim();

        if(email.isEmpty()) {
            editTextEmail.setError("Email address is required.");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email address.");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(ForgotPassword.this, "Please check your email to reset your password.", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(ForgotPassword.this, "Password reset failed. Please try again.", Toast.LENGTH_LONG).show(); // TODO: verify account doesn't exist
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}