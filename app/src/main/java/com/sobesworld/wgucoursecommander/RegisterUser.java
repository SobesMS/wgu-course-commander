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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterUser extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        editTextFirstName = findViewById(R.id.first_name_reg);
        editTextLastName = findViewById(R.id.last_name_reg);
        editTextEmail = findViewById(R.id.email_reg);
        editTextPassword = findViewById(R.id.password_reg);

        progressBar = findViewById(R.id.reg_progress);

        Button registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(view -> registerUser());

        Button cancelBtn = findViewById(R.id.cancel_button_reg);
        cancelBtn.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterUser.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(firstName.isEmpty()) {
            editTextFirstName.setError("First name is required.");
            editTextFirstName.requestFocus();
            return;
        }

        if(lastName.isEmpty()) {
            editTextLastName.setError("Last name is required.");
            editTextLastName.requestFocus();
            return;
        }

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

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        User user = new User(firstName, lastName, email);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                .setValue(user).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        firebaseUser.sendEmailVerification();
                                        mAuth.signOut();
                                        Toast.makeText(RegisterUser.this, "User registration successful! Please check your email to verify your account.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(RegisterUser.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterUser.this, "User registration failed. Please try again.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                    } else {
                        Toast.makeText(RegisterUser.this, "User registration failed. Please try again.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }
}