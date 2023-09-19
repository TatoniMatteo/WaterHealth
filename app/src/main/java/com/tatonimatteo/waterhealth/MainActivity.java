package com.tatonimatteo.waterhealth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tatonimatteo.waterhealth.configuration.AppConfiguration;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppConfiguration.init(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestNotificationPermission();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNotificationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)) {
            showPermissionRationaleDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_NOTIFICATION_PERMISSION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void showPermissionRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Richiesta di permesso")
                .setMessage("L'app ha bisogno del tuo permesso per notificare in tempo reale eventuali parametri fuori soglia.")
                .setPositiveButton("Concedi", (dialogInterface, i) -> ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION))
                .setNegativeButton("Ignora", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }


}
