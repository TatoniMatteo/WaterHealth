package com.tatonimatteo.waterhealth.api.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.tatonimatteo.waterhealth.MainActivity;
import com.tatonimatteo.waterhealth.R;

public class NotificationUtils {

    private static final String CHANNEL_ID = "WH";
    private static final String CHANNEL_NAME = "Whater Health Channel";
    private static final String CHANNEL_DESCRIPTION = "Channel for Water Health App.";

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static void showNotification(Context context, String title, String content) {
        createNotificationChannel(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_logo)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntent(context))
                .setAutoCancel(true);

        builder.addAction(R.drawable.see, "Vedi nell'applicazione", getPendingIntent(context));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private static void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription(CHANNEL_DESCRIPTION);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private static PendingIntent getPendingIntent(Context context) {
        Intent intent;
        intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}
