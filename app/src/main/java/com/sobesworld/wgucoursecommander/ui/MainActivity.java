package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
    public static final String EXTRA_REQUEST_ID = "com.sobesworld.wgucoursecommander.EXTRA_REQUEST_ID";
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    public static final int RESULT_DELETE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
    }

    public void goToTermList(View view) {
        Intent intent= new Intent(MainActivity.this, TermList.class);
        startActivity(intent);
    }

    public void goToCourseList(View view) {
        Intent intent= new Intent(MainActivity.this, CourseList.class);
        startActivity(intent);
    }

    public void goToAssessmentList(View view) {
        Intent intent= new Intent(MainActivity.this, AssessmentList.class);
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