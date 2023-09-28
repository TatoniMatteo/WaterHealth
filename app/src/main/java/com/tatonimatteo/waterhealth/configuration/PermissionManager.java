package com.tatonimatteo.waterhealth.configuration;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class PermissionManager {

    public static final String NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    public static void checkAndRequestNotificationPermission(Activity activity) {
        int permissionState = ContextCompat.checkSelfPermission(activity, NOTIFICATION_PERMISSION);

        if (permissionState != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, NOTIFICATION_PERMISSION)) {
                showNotificationPermissionRationaleDialog(activity);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{NOTIFICATION_PERMISSION},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }

    private static void showNotificationPermissionRationaleDialog(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Richiesta di permesso")
                .setMessage("L'app ha bisogno del tuo permesso per mostrare le notifiche.")
                .setPositiveButton(
                        "Concedi",
                        (dialog, which) -> {
                            ActivityCompat.requestPermissions(
                                    activity,
                                    new String[]{NOTIFICATION_PERMISSION},
                                    REQUEST_NOTIFICATION_PERMISSION
                            );
                            Toast.makeText(activity, "Permesso di notifica concesso!", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                )
                .setNegativeButton("Ignora", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
