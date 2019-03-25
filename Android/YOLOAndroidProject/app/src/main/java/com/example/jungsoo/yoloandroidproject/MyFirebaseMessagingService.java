package com.example.jungsoo.yoloandroidproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.content.ContentValues.TAG;
// 메세지 수신 이 firebase 사이트에서 참고 :  https://firebase.google.com/docs/cloud-messaging/android/receive?hl=ko


public class MyFirebaseMessagingService extends FirebaseMessagingService {  // onMessageReceived 함수 참조하여 내부 구현

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // 메세지 받았을 때 함수처리 하는 부분. default는 그냥 위에 알림만 뜸.


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            sendPushNotification(remoteMessage);  // sendPushNotification은 알림을 받는 역할을 하는 함수. remoteMessage는 firebase를 통해 밖에서 들어온 것.

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }




    private void handleNow() {
    }

    private void scheduleJob() {
    }


    // 메세지 들어왔을 때 알림하는 부분!!!!!

    private void sendPushNotification(RemoteMessage remoteMessage) { // sendPushNotification() 내부 수정 구현.

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon).setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.mipmap.sym_def_app_icon))
                .setContentTitle(remoteMessage.getData().get("title")) // 메세지 내용!!
                .setContentText(remoteMessage.getData().get("text"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255,500,2000) // 알림 소리!!
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("msg",s);
    }

}

