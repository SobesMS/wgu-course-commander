package com.sobesworld.wgucoursecommander;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.ui.NavMenu;

public class ViewEditProfile extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private User dbUserRecord;
    private String userID;
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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null) {
            Intent intent = new Intent(ViewEditProfile.this, MainActivity.class);
            startActivity(intent);
        } else {
            userID = firebaseUser.getUid();

            database.child("Users").child(userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dbUserRecord = task.getResult().getValue(User.class);
                    if (dbUserRecord == null) {
                        Toast.makeText(ViewEditProfile.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ViewEditProfile.this, NavMenu.class);
                        startActivity(intent);
                    } else {
                        editTextFirstName.setText(dbUserRecord.getFirstName());
                        editTextLastName.setText(dbUserRecord.getLastName());
                        editTextEmail.setText(dbUserRecord.getEmail());
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

        if (firstName.isEmpty()) {
            editTextFirstName.setError("First name was left blank. Re-adding from database.");
            editTextFirstName.setText(dbUserRecord.getFirstName());
            return;
        }

        if (lastName.isEmpty()) {
            editTextLastName.setError("Last name was left blank. Re-adding from database");
            editTextLastName.setText(dbUserRecord.getLastName());
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email was left blank. Re-adding from database.");
            editTextEmail.setText(dbUserRecord.getEmail());
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid email format.");
            return;
        }

        User newProfile = new User(firstName, lastName, email);

        if (firstName.equalsIgnoreCase(dbUserRecord.getFirstName()) && lastName.equalsIgnoreCase(dbUserRecord.getLastName())
                && email.equalsIgnoreCase(dbUserRecord.getEmail())) {
            progressBar.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(ViewEditProfile.this, NavMenu.class);
            startActivity(intent);
        } else {
            if (email.equalsIgnoreCase(dbUserRecord.getEmail())) {
                database.child("Users").child(userID).setValue(newProfile).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ViewEditProfile.this, "Profile edit successful.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ViewEditProfile.this, NavMenu.class);
                        startActivity(intent);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ViewEditProfile.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                database.child("Users").child(userID).setValue(newProfile).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.updateEmail(newProfile.getEmail()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(ViewEditProfile.this, "Profile edit successful.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ViewEditProfile.this, NavMenu.class);
                                startActivity(intent);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(ViewEditProfile.this, "An error occurred while updating email. Please notify admin.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ViewEditProfile.this, NavMenu.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ViewEditProfile.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void deleteAccount() {
        progressBar.setVisibility(View.VISIBLE);

        TermViewModel termViewModel = new ViewModelProvider(ViewEditProfile.this).get(TermViewModel.class);
        CourseViewModel courseViewModel = new ViewModelProvider(ViewEditProfile.this).get(CourseViewModel.class);
        AssessmentViewModel assessmentViewModel = new ViewModelProvider(ViewEditProfile.this).get(AssessmentViewModel.class);

        AlertDialog.Builder dialog = new AlertDialog.Builder(ViewEditProfile.this);
        dialog.setTitle("CONFIRM ACCOUNT DELETION")
                .setMessage("Select CONFIRM to delete your user account and all related records. Select ABORT to cancel.\n\n(records are not recoverable)")
                .setCancelable(false)
                .setNegativeButton("ABORT", (dialogInterface, i) -> {
                })
                .setPositiveButton("CONFIRM", (dialogInterface, i) -> firebaseUser.delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        termViewModel.deleteAllTermsByUserID(userID);
                        courseViewModel.deleteAllCoursesByUserID(userID);
                        assessmentViewModel.deleteAllAssessmentsByUserID(userID);
                        database.child("Users").child(userID).removeValue();
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ViewEditProfile.this, "User has been deleted.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ViewEditProfile.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ViewEditProfile.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }))
                .show();
    }
}