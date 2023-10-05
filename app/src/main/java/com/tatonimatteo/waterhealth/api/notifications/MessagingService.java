package com.tatonimatteo.waterhealth.api.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("Firebase", "Refreshed token: " + token);
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getData().size() > 0) {
            handleDataMessage(message.getData());
        }

        if (message.getNotification() != null) {
            handleNotification(message.getNotification());
        }
    }

    private void handleDataMessage(Map<String, String> data) {
        String title = data.get("title");
        String content = data.get("content");
        showNotification(title, content);
    }

    private void handleNotification(RemoteMessage.Notification notification) {
        String title = notification.getTitle();
        String body = notification.getBody();
        showNotification(title, body);
    }

    private void showNotification(String title, String content) {
        NotificationUtils.showNotification(this, title, content);
    }
}
