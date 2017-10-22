package com.pashkobohdan.promocodesmanager.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.pashkobohdan.promocodesmanager.R;

public class DialogUtils {

    private DialogUtils() {
        //Utility class
    }

    public interface ClickCallback {
        void call();
    }

    public static void showAlert(@NonNull Context context, String title, String text, final ClickCallback okClickCallback, final ClickCallback cancelClickCallback) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title == null ? context.getString(R.string.default_alert_title) : title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (okClickCallback != null) okClickCallback.call();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (cancelClickCallback != null) cancelClickCallback.call();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showConfirm(@NonNull Context context, String title, String text, final ClickCallback okClickCallback, final ClickCallback cancelClickCallback) {
        showAlert(context, title == null ? context.getString(R.string.default_confirm_title) : title, text, okClickCallback, cancelClickCallback);
    }

    public interface Callback<T> {
        void call(T t);
    }

    public static void showInputDialog(@NonNull Context context, String title, @NonNull final Callback<String> newAppNameCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newAppNameCallback.call(input.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public static void showMultiLineInputDialog(@NonNull Context context, String title, @NonNull final Callback<String> newAppNameCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(context).inflate(R.layout.multi_line_input_dialog, null);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newAppNameCallback.call(input.getText().toString());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
