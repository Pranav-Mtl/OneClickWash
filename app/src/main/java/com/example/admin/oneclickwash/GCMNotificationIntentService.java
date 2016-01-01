package com.example.admin.oneclickwash;

/**
 * Created by Pranav Mittal on 9/23/2015.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;


import com.example.admin.oneclickwash.Constant.Constant;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GCMNotificationIntentService extends IntentService {
    // Sets an ID for the notification, so it can be updated

    //private NotificationManager mNotificationManager;

    public static final int notifyID = 9001;
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                    sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                try {

                    System.out.println("GCM MESSAGE" + "" + extras.get(Constant.MSG_KEY));

                    String msgArray = extras.get(Constant.MSG_KEY) + "";
                    try {
                        JSONObject jsonObj = new JSONObject(msgArray);
                        String notification_type = jsonObj.getString("notification_type");

                         if(notification_type.equals("subscription")){
                            sendNotificationRoute(msgArray);
                        }
                        else if(notification_type.equals("order")){
                            sendNotificationBooking(msgArray);
                        }
                         else if(notification_type.equals("image")){
                             sendNotificationImage(msgArray);
                         }





                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }


            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }



    private void sendNotificationRoute(String greetMsg) {

        String id="";
        String message="";
        String title="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);
            message=jsonObj.getString("message");
            title=jsonObj.getString("title");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, HomeScreen.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentTitle(title);
        mNotifyBuilder.setContentText(message);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }

    private void sendNotificationBooking(String greetMsg) {

        String id="";
        String message="";
        String title="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);
            message=jsonObj.getString("message");
            title=jsonObj.getString("title");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, HomeScreen.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentTitle(title);
        mNotifyBuilder.setContentText(message);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }




    private void sendNotification(String greetMsg) {

        String id="";
        String message="";
        String title="";

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);

            id=jsonObj.getString("last_id");
            message=jsonObj.getString("greetMsg");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, HomeScreen.class);
        resultIntent.putExtra("ID", id);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Loop")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentTitle("");
        mNotifyBuilder.setContentText(message);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }

    /* SEND NOTIFICATION IMAGE*/

    private void sendNotificationImage(String greetMsg) {

        String message="";
        String title="";
        String imageURL="";
        Bitmap remote_picture = null;

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);
            message=jsonObj.getString("message");
            title=jsonObj.getString("title");
            imageURL=jsonObj.getString("image");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setSummaryText(message);

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        notiStyle.bigPicture(remote_picture);

        Intent resultIntent = new Intent(this, HomeScreen.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(notiStyle);


        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentTitle(title);
        mNotifyBuilder.setContentText(message);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }


}

