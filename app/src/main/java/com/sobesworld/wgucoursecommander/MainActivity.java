package com.sobesworld.wgucoursecommander;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sobesworld.wgucoursecommander.ui.NavMenu;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email_login);
        editTextPassword = findViewById(R.id.password_login);
        progressBar = findViewById(R.id.login_progress);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> login());

        TextView forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
            startActivity(intent);
        });

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegisterUser.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Intent intent = new Intent(MainActivity.this, NavMenu.class);
            startActivity(intent);
        }
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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

        if(password.isEmpty()) {
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editTextPassword.setError("Password must be 6 or more characters.");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        assert firebaseUser != null;
                        if(firebaseUser.isEmailVerified()) {
                            Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(MainActivity.this, NavMenu.class);
                            startActivity(intent);
                        } else {
                            firebaseUser.sendEmailVerification();
                            Toast.makeText(MainActivity.this, "Check your email to verify your account.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        String characterChoices = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvwxyz"
                + "!@#$%^&*()";

        for (int i = 0; i < length; i++) {
            int index = (int)(characterChoices.length() * Math.random());
            sb.append(characterChoices.charAt(index));
        }

        return sb.toString();
    }
}