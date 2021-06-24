package com.abd.speechcontrolledassistance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class About{

//    static String text = R.string.about_text;

    public static void show(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("About");
        builder.setMessage(R.string.about_text);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog dialog = builder.create();

        Log.d(MainActivity.LOG_TAG, "Showing About!");
        dialog.show();
//        ref: https://www.tutorialspoint.com/android/android_alert_dialoges.htm
    }
}
