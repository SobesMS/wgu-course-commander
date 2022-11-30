package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sobesworld.wgucoursecommander.MainActivity;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.SearchActivity;
import com.sobesworld.wgucoursecommander.User;
import com.sobesworld.wgucoursecommander.ViewEditProfile;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NavMenu extends AppCompatActivity {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
    public static final String SHARED_PREFS_FILENAME = "com.sobesworld.wgucoursecommander.prefs";
    public static final String SHARED_PREFS_ALERT_ID_COUNTER = "alert_id_counter";
    public static final String EXTRA_REQUEST_ID = "com.sobesworld.wgucoursecommander.EXTRA_REQUEST_ID";
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;

    private DatabaseReference database;
    private TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_menu);

        // creates notification channel for course and assessment alerts
        createNotificationChannel();
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.contains(SHARED_PREFS_ALERT_ID_COUNTER)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SHARED_PREFS_ALERT_ID_COUNTER, 101).apply();
        }

        // retrieves user database reference
        database = FirebaseDatabase.getInstance().getReference();

        // UI elements
        greeting = findViewById(R.id.greeting);

        Button termListBtn = findViewById(R.id.main_terms_button);
        termListBtn.setOnClickListener(view -> goToTermList());

        Button courseListBtn = findViewById(R.id.main_courses_button);
        courseListBtn.setOnClickListener(view -> goToCourseList());

        Button assessmentListBtn = findViewById(R.id.main_assessments_button);
        assessmentListBtn.setOnClickListener(view -> goToAssessmentList());

        TextView logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(view -> logout());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // confirms a user is currently logged in and personalizes the greeting
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Toast.makeText(NavMenu.this, "User logged out. Please try again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(NavMenu.this, MainActivity.class));
        } else {
            database.child("Users").child(user.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User userProfile = task.getResult().getValue(User.class);
                    if (userProfile == null) {
                        greeting.setText(R.string.welcome);
                    } else {
                        String firstNameGreeting = ("Welcome, " + userProfile.getFirstName() + "!");
                        greeting.setText(firstNameGreeting);
                    }
                } else {
                    greeting.setText(R.string.welcome);
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navmenu_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.view_profile_btn) {
            startActivity(new Intent(NavMenu.this, ViewEditProfile.class));
        }
        if (item.getItemId() == R.id.search) {
            startActivity(new Intent(NavMenu.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    // methods for navigation buttons
    private void goToTermList() {
        startActivity(new Intent(NavMenu.this, TermList.class));
    }

    private void goToCourseList() {
        startActivity(new Intent(NavMenu.this, CourseList.class));
    }

    private void goToAssessmentList() {
        startActivity(new Intent(NavMenu.this, AssessmentList.class));
    }

    private void logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(NavMenu.this);
        dialog.setTitle("LOGOUT CONFIRMATION")
                .setMessage("Do you really want to log out of WGU Course Commander?")
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(NavMenu.this, "Logout successful!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NavMenu.this, MainActivity.class);
                    startActivity(intent);
                })
                .show();
    }

    private void createNotificationChannel() {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CourseCommReceiver.CHANNEL_ID, CourseCommReceiver.CHANNEL_NAME,
                importance);
        channel.setDescription(CourseCommReceiver.CHANNEL_DESCRIPTION);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}