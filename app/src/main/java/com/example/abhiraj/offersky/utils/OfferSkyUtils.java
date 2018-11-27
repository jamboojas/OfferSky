package com.example.abhiraj.offersky.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.model.Mall;
import com.google.gson.Gson;

/**
 * Created by Abhiraj on 30-06-2017.
 */

public class OfferSkyUtils {

    private static final String TAG = OfferSkyUtils.class.getSimpleName();

    public static ProgressDialog mProgressDialog;

    public static String getCurrentMallId(Context context){
        // get the mall object
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                Context.MODE_PRIVATE);
        String mallId = sharedPreferences.getString(Constants.SharedPreferences.MALL_ID, "MH_0253_CCM");
        return mallId;
    }

    public static Mall getCurrentMall(Context context){
        Log.d(TAG, "getMall()");

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                        Context.MODE_PRIVATE);
        String mall_string = sharedPreferences.getString(Constants.SharedPreferences.MALL_JSON, "");
        Gson mall_gson = new Gson();
        Mall mall = mall_gson.fromJson(mall_string, Mall.class);
        return mall;
    }

    public static NotificationCompat.Builder createNotificationBuilder(Context context, String title, String msg,  PendingIntent notificationPendingIntent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.RED)
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return builder;
    }

    public static void showProgressDialog(Context context, String message) {
        //if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        //}

        mProgressDialog.show();
    }


    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
