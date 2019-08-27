package com.elblasy.navigation.services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.elblasy.navigation.R;
import com.elblasy.navigation.SpeackToDriver;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID = "Wasayl";
    NotificationManager notificationManager;

    public MyFireBaseMessagingService() {
    }


    @Override
    public void onNewToken(@NotNull String s) {
        super.onNewToken(s);

        FirebaseMessaging.getInstance().subscribeToTopic("wasaylclient");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("MyFireBaseMessaging", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    Log.e("My Token", token);
                });
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String placeName = remoteMessage.getData().get("title");

        final Intent intent = new Intent(this, SpeackToDriver.class);
        intent.putExtra("placeName", placeName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels();
        }

        int notificationId = new Random().nextInt(60000);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_ic_notification) //a resource for your custom small icon
                .setContentTitle(remoteMessage.getData().get("title")) //the "title" value you sent in your notification
                .setContentText(remoteMessage.getData().get("message")) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {

        CharSequence adminChannelName = "Messages";
        String adminChannelDescription = "Receive messages for your order";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(getResources().getColor(R.color.colorAccent));
        adminChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        adminChannel.setShowBadge(true);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }

    }
}
