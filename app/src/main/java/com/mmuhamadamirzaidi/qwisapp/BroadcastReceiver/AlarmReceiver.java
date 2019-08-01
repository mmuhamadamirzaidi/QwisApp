package com.mmuhamadamirzaidi.qwisapp.BroadcastReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.mmuhamadamirzaidi.qwisapp.Home;
import com.mmuhamadamirzaidi.qwisapp.R;

public class AlarmReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        long currentSystemTime = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Change intent for student soon
        Intent notificationIntent = new Intent(context, Home.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Qwis")
                .setContentText("Hey! Try to solve quiz today!")
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setWhen(currentSystemTime)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});

        notificationManager.notify(0, builder.build());

    }
}
