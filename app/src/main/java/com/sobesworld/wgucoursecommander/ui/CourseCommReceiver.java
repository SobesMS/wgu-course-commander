package com.sobesworld.wgucoursecommander.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.sobesworld.wgucoursecommander.R;

public class CourseCommReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "WGUCourseCommID";
    public static final CharSequence CHANNEL_NAME = "WGU Course Commander Notification Channel";
    public static final String CHANNEL_DESCRIPTION = "This is the notification channel for the WGU Course Commander app.";
    public static final String EXTRA_BROADCAST_ALERT_ID = "com.sobesworld.wgucoursecommander.EXTRA_BROADCAST_ALERT_ID";
    public static final String EXTRA_BROADCAST_TITLE = "com.sobesworld.wgucoursecommander.EXTRA_BROADCAST_TITLE";
    public static final String EXTRA_BROADCAST_TEXT = "com.sobesworld.wgucoursecommander.EXTRA_BROADCAST_TEXT";

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(EXTRA_BROADCAST_ALERT_ID, -1);
        createNotificationChannel(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_school)
                .setContentTitle(intent.getStringExtra(EXTRA_BROADCAST_TITLE))
                .setContentText(intent.getStringExtra(EXTRA_BROADCAST_TEXT))
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    private void createNotificationChannel(Context context) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
        channel.setDescription(CHANNEL_DESCRIPTION);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
