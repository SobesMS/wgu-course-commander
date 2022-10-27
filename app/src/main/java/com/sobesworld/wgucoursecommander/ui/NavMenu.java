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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sobesworld.wgucoursecommander.MainActivity;
import com.sobesworld.wgucoursecommander.R;
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
    public static final int RESULT_DELETE = 99;

    private DatabaseReference database;
    TextView greeting, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_menu);
        createNotificationChannel();
        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.contains(SHARED_PREFS_ALERT_ID_COUNTER)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SHARED_PREFS_ALERT_ID_COUNTER, 101).apply();
        }

        database = FirebaseDatabase.getInstance().getReference();
        greeting = findViewById(R.id.greeting);
        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(view -> logout());
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent intent = new Intent(NavMenu.this, MainActivity.class);
            startActivity(intent);
        } else {
            database.child("Users").child(user.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    User userProfile = task.getResult().getValue(User.class);
                    if (userProfile == null) {
                        greeting.setText(R.string.welcome);
                    } else {
                        String firstNameGreeting = ("Welcome, " + userProfile.firstName + "!");
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
            viewProfile();
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewProfile() {
        Intent intent = new Intent(NavMenu.this, ViewEditProfile.class);
        startActivity(intent);
    }

    public void logout() {
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

    public void goToTermList(View view) {
        Intent intent= new Intent(NavMenu.this, TermList.class);
        startActivity(intent);
    }

    public void goToCourseList(View view) {
        Intent intent= new Intent(NavMenu.this, CourseList.class);
        startActivity(intent);
    }

    public void goToAssessmentList(View view) {
        Intent intent= new Intent(NavMenu.this, AssessmentList.class);
        startActivity(intent);
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