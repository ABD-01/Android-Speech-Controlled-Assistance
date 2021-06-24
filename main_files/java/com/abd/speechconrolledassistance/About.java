package com.abd.speechconrolledassistance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class About{

    static String text = "Speech Controlled Assistance.\nUses Voice commands to ON/OFF household appliances.";

    public static void show(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("About");
        builder.setMessage(text);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        System.out.println("Showing About");
        AlertDialog dialog = builder.create();

        Log.d(MainActivity.LOG_TAG, "Showing About!");
        dialog.show();
//        ref: https://www.tutorialspoint.com/android/android_alert_dialoges.htm
    }
}