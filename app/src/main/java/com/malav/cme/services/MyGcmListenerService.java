package com.malav.cme.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.malav.cme.R;
import com.malav.cme.activity.DashboardActivity;

/**
 * Created by shahmalav on 12/09/16.
 */

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    String pageName="nothing", role="";
    public static String filename = "MySharedString";
    private SharedPreferences someData;
    String commonL_Id, message;
    Context context;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        context  = getApplicationContext();
        someData = getSharedPreferences(filename, 0);
        commonL_Id = someData.getString("commonL_Id", "");

        message = data.getString("message");
        pageName = data.getString("pageName");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "data: " + data);
        Log.d(TAG, "pageName: " + pageName);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {

        message = message.replace("\\","");

        someData = getSharedPreferences(filename, 0);
        if(someData.contains("role")){
            role = someData.getString("role","");
        }else{
            role = "Admin";
        }

        Intent intent;
        intent = new Intent(this, DashboardActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.com_facebook_button_icon)
                .setContentTitle("Holistree")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setOngoing(true);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notId = (int) System.currentTimeMillis()%10000;

        notificationManager.notify(notId /* ID of notification */, notificationBuilder.build());
    }
}