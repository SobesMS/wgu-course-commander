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

        // retrieves login authorization reference
        mAuth = FirebaseAuth.getInstance();

        // UI elements
        editTextEmail = findViewById(R.id.email_login);
        editTextPassword = findViewById(R.id.password_login);

        progressBar = findViewById(R.id.login_progress);

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> login());

        TextView forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ForgotPassword.class)));

        TextView register = findViewById(R.id.register);
        register.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RegisterUser.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // confirms a user is currently logged in and will bypass login if true
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            startActivity(new Intent(MainActivity.this, NavMenu.class));
        }
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // validates editText fields
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

        // authenticates user login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser != null;
                        if(firebaseUser.isEmailVerified()) {
                            Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(MainActivity.this, NavMenu.class));
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
}