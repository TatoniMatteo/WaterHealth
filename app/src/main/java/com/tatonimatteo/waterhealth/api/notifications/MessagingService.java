package com.tatonimatteo.waterhealth.api.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("Firebase", "Refreshed token: " + token);
        super.onNewToken(token);
    }
}
