package com.example.abhiraj.offersky.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Abhiraj on 13-08-2017.
 */

public class CheckNetworkConnectivity extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = CheckNetworkConnectivity.class.getSimpleName();

    public interface ConnectionResponse{
        void networkCheckResult(boolean result);
    }

    private ConnectionResponse mConnectionResponse = null;
    private Context mContext;

    public CheckNetworkConnectivity(ConnectionResponse response, Context context){
        mConnectionResponse = response;
        mContext = context;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(mConnectionResponse != null){
            mConnectionResponse.networkCheckResult(aBoolean);
        }
    }
}
