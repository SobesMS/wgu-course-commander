package com.sobesworld.wgucoursecommander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sobesworld.wgucoursecommander.ui.NavMenu;

public class ViewEditProfile extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail;
    private FirebaseUser user;
    private DatabaseReference database;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_edit_profile);

        database = FirebaseDatabase.getInstance().getReference();
        editTextFirstName = findViewById(R.id.first_name_viewprof);
        editTextLastName = findViewById(R.id.last_name_viewprof);
        editTextEmail = findViewById(R.id.email_viewprof);
        progressBar = findViewById(R.id.viewprof_progress);

        Button saveProfileBtn = findViewById(R.id.save_prof_btn);
        saveProfileBtn.setOnClickListener(view -> saveProfile());

        Button deleteProfileBtn = findViewById(R.id.delete_account_button);
        deleteProfileBtn.setOnClickListener(view -> deleteAccount());
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            Intent intent = new Intent(ViewEditProfile.this, MainActivity.class);
            startActivity(intent);
        } else {
            database.child("Users").child(user.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User userProfile = task.getResult().getValue(User.class);
                    if (userProfile == null) {
                        Toast.makeText(ViewEditProfile.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ViewEditProfile.this, NavMenu.class);
                        startActivity(intent);
                    } else {
                        editTextFirstName.setText(userProfile.firstName);
                        editTextLastName.setText(userProfile.lastName);
                        editTextEmail.setText(userProfile.email);
                    }
                } else {
                    Toast.makeText(ViewEditProfile.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ViewEditProfile.this, NavMenu.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void saveProfile() {
        progressBar.setVisibility(View.VISIBLE);
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        database.child("Users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    User userProfile = task.getResult().getValue(User.class);
                    if (userProfile == null) {
                        Toast.makeText(ViewEditProfile.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        String dbFirstName = userProfile.firstName;
                        String dbLastName = userProfile.lastName;
                        String dbEmail = userProfile.email;

                        if (firstName.equals(dbFirstName) && lastName.equals(dbLastName) && email.equalsIgnoreCase(dbEmail)) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(ViewEditProfile.this, NavMenu.class);
                            startActivity(intent);
                        } else {
                            if (!firstName.equals(dbFirstName)) {
                                if (firstName.isEmpty()) {
                                    editTextFirstName.setError("First name was left blank.");
                                    editTextFirstName.setText(dbFirstName);
                                    return;
                                } else {
                                    userProfile.setFirstName(firstName);
                                }
                            }

                            if (!lastName.equals(dbLastName)) {
                                if (lastName.isEmpty()) {
                                    editTextLastName.setError("Last name was left blank.");
                                    editTextLastName.setText(dbLastName);
                                    return;
                                } else {
                                    userProfile.setLastName(lastName);
                                }
                            }

                            if (!email.equalsIgnoreCase(dbEmail) && !email.isEmpty()) {
                                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    editTextEmail.setError("Invalid email format.");
                                    editTextEmail.requestFocus();
                                    return;
                                } else {
                                    // TODO: update db and account email address
                                    userProfile.setEmail(email);
                                }
                            }

                            database.child("Users").child(user.getUid()).setValue(userProfile).addOnCompleteListener(task1 -> {
                                Toast.makeText(ViewEditProfile.this, "Profile edit successful.", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(ViewEditProfile.this, NavMenu.class);
                                startActivity(intent);
                            });
                        }
                    }
                }
            }
        });
    }

    private void deleteAccount() {
        progressBar.setVisibility(View.VISIBLE);

        if (user == null) {
            Toast.makeText(ViewEditProfile.this, "Unable to delete account. Please try again later.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            database.child("Users").child(user.getUid()).removeValue().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    FirebaseAuth.getInstance().signOut();
                                    Toast.makeText(ViewEditProfile.this, "Account deletion successful.", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(ViewEditProfile.this, MainActivity.class);
                                    startActivity(intent);
                                } // TODO: else logic - account deleted but db record is not
                            });
                        }
                    });
        }
    }
}