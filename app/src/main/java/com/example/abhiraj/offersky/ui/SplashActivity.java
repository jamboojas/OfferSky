package com.example.abhiraj.offersky.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.signup.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                Context.MODE_PRIVATE);
        String mallId = sharedPreferences.getString(Constants.SharedPreferences.MALL_ID, "No mall Found");

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            // See if the user has previously selected any mall
            // if yes then start the main activity
            if(!mallId.equals("No mall Found")){
                Log.d(TAG, "Mall already selected = " + mallId);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Log.d(TAG, "no mall selected before = " + mallId);
                Intent intent = new Intent(this, MallSelectActivity.class);
                startActivity(intent);
            }
        }

        else{
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
