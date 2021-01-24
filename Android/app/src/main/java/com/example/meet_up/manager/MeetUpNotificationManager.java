package com.example.meet_up.manager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.meet_up.R;

public class MeetUpNotificationManager {

    private static final String CHANNEL_ID = "meet_up.notification_channel";

    private static MeetUpNotificationManager mInstance;
    private Context mContext;
    private NotificationChannel mChannel;

    private MeetUpNotificationManager(Context context) {
        mContext = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = mContext.getString(R.string.channel_name);
            mChannel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    public NotificationCompat.Builder getOngoingNotificationBuilder() {
        return new  NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Meet up")
                .setContentText("Your location is constantly being updated")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);
    }

    public static MeetUpNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MeetUpNotificationManager(context);
        }
        return mInstance;
    }
}
