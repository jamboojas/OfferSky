package com.example.abhiraj.offersky.receivers;

/**
 * Created by Abhiraj on 16-06-2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.abhiraj.offersky.Constants;


public class GPSStateReceiver extends BroadcastReceiver {

    private static String TAG = GPSStateReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, intent.getAction().toString() + " intent action received in the receiver class");
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Intent gpsIntent = new Intent();
        if(isGPSEnabled){
            Log.d(TAG, "GPS is enabled in receiver class");

            gpsIntent.setAction(Constants.Location.GPS_STATE_ON_BROADCAST);
            LocalBroadcastManager.getInstance(context).sendBroadcast(gpsIntent);
        }
        else{
            Log.d(TAG, "GPS is disabled in receiver class");
            gpsIntent.setAction(Constants.Location.GPS_STATE_OFF_BROADCAST);
            LocalBroadcastManager.getInstance(context).sendBroadcast(gpsIntent);
        }
    }
}

