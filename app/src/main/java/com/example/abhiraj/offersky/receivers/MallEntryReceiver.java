package com.example.abhiraj.offersky.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.abhiraj.offersky.Constants;

/**
 * Created by Abhiraj on 24-04-2017.
 */

public class MallEntryReceiver extends BroadcastReceiver {

    public static final String TAG = MallEntryReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE, Context.MODE_PRIVATE);
        
    }
}
