package com.tatonimatteo.waterhealth.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class PopUp {

    private final Context context;

    public PopUp(Context context) {
        this.context = context;
    }

    public void showErrorPopup(
            String errorTitle,
            String errorMessage,
            CharSequence positiveButtonText,
            DialogInterface.OnClickListener positiveListener,
            CharSequence negativeButtonText,
            DialogInterface.OnClickListener negativeListener,
            boolean cancellable
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(errorTitle)
                .setMessage(errorMessage)
                .setPositiveButton(positiveButtonText, positiveListener)
                .setNegativeButton(negativeButtonText, negativeListener)
                .setCancelable(cancellable)
                .show();
    }

    public void showConfirmationPopup(
            String title,
            String message,
            CharSequence positiveButtonText,
            DialogInterface.OnClickListener positiveListener,
            CharSequence negativeButtonText,
            DialogInterface.OnClickListener negativeListener,
            boolean cancellable
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveListener)
                .setNegativeButton(negativeButtonText, negativeListener)
                .setCancelable(cancellable)
                .show();
    }

    public void showInfoPopup(
            String title,
            String message,
            CharSequence positiveButtonText,
            DialogInterface.OnClickListener positiveListener,
            boolean cancellable
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveListener)
                .setCancelable(cancellable)
                .show();
    }
}
