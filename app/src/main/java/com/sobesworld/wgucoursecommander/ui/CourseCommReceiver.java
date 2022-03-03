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
    public static final String CHANNEL_ID = "com.sobesworld.wgucoursecommander";
    public static final CharSequence CHANNEL_NAME = "WGU Course Commander Alerts";
    public static final String CHANNEL_DESCRIPTION = "Broadcast receiver for the WGU Course Commander app.";
    public static final String EXTRA_NOTIFICATION_ID = "com.sobesworld.wgucoursecommander.EXTRA_NOTIFICATION_ID";
    public static final String EXTRA_NOTIFICATION_TITLE = "com.sobesworld.wgucoursecommander.EXTRA_NOTIFICATION_TITLE";
    public static final String EXTRA_NOTIFICATION_BODY = "com.sobesworld.wgucoursecommander.EXTRA_NOTIFICATION_BODY";

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1);
        createNotificationChannel(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_school)
                .setContentTitle(intent.getStringExtra(EXTRA_NOTIFICATION_TITLE))
                .setContentText(intent.getStringExtra(EXTRA_NOTIFICATION_BODY))
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
