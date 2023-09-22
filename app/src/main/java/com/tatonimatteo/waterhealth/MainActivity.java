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

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private static final String PERMISSION = Manifest.permission.POST_NOTIFICATIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppConfiguration.init(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAndRequestPermission();
    }

    private void checkAndRequestPermission() {
        int permissionState = ContextCompat.checkSelfPermission(this, PERMISSION);

        if (permissionState != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
                showPermissionRationaleDialog();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{PERMISSION},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }

    private void showPermissionRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Richiesta di permesso")
                .setMessage("L'app ha bisogno del tuo permesso per eseguire alcune operazioni.")
                .setPositiveButton(
                        "Concedi",
                        (dialog, which) -> ActivityCompat.requestPermissions(
                                MainActivity.this,
                                new String[]{PERMISSION},
                                REQUEST_NOTIFICATION_PERMISSION)
                )
                .setNegativeButton("Ignora", (dialog, which) -> {
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

