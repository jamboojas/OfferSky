package com.example.abhiraj.offersky;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Abhiraj on 18-04-2017.
 */

public class OfferSkyApplication extends Application {

    private static final String TAG = OfferSkyApplication.class.getSimpleName();

    @Override
    public void onCreate(){
        Log.v(TAG, "onCreate()");
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SharedPreferences.EARNING_STATUS, Constants.SharedPreferences.NOT_EARNING);
        editor.commit();
    }


}
