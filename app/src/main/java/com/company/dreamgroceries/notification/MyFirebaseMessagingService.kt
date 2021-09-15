package com.company.dreamgroceries.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.company.dreamgroceries.R
import com.company.dreamgroceries.login.SplashActivity
import com.company.dreamgroceries.utilities.ACTION_UPDATE_NOT_COUNT
import com.company.dreamgroceries.utilities.PreferenceHelper
import com.company.dreamgroceries.utilities.PreferenceHelper.setValue
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val NOTIFICATION_CHANNEL_ID: String = "order_channel"
    private val CHANNEL_ID: String = "notification"

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.e("newToken", s)
        setValue(PreferenceHelper.FCM_TOKEN, s, baseContext)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("message", "on messsage received ")
        val intent = Intent()
        intent.setAction(ACTION_UPDATE_NOT_COUNT)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        if (remoteMessage.notification != null) {
            showNotification(
                remoteMessage,
                remoteMessage.notification?.title,
                remoteMessage.notification?.body,
                remoteMessage.data.get("order_no")
            )
        }
    }

    private fun showNotification(
        remoteMessage: RemoteMessage,
        title: String?,
        body: String?,
        orderno: String?
    ) {
        Log.e("tag", "in show notification " + remoteMessage.toString())
        val intent = Intent(this, SplashActivity::class.java)
        intent.putExtra("order_no", orderno)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_dis)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentTitle(title)
            .setContentText(body)
           // .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        /*mBuilder = NotificationCompat.Builder(this,CHANNEL_ID);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(false)

            .setContentIntent(resultPendingIntent);*/
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH;
            val notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, "Order channel", importance);
            notificationChannel.enableVibration(true);

            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        } else {
            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }




        notificationManager.notify(remoteMessage.sentTime.toInt(), notificationBuilder.build())

        Log.e("tag", "in show notification complete")
    }
}


