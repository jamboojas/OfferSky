package com.example.abhiraj.offersky.geofencing;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.compat.BuildConfig;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.DbHandler.CouponDbHandler;
import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.model.Coupon;
import com.example.abhiraj.offersky.model.Mall;
import com.example.abhiraj.offersky.ui.MainActivity;
import com.example.abhiraj.offersky.utils.FirebaseUtils;
import com.example.abhiraj.offersky.utils.NotificationUtils;
import com.example.abhiraj.offersky.utils.OfferSkyUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Abhiraj on 22-04-2017.
 */

public class StepListener extends Service implements SensorEventListener, AccelerometerStepListener{

    private static final String TAG = StepListener.class.getSimpleName();
    private static int steps;

    private static NotificationCompat.Builder notificationBuilder;

    private static PowerManager powerManager;
    private static PowerManager.WakeLock wakeLock;

    private static ArrayList<Coupon> couponsToOffer = new ArrayList<>();
    private static long visitor_no=0;
    private static int no_of_coupons_to_give = 5;
    private static int M1 = 0, M2 = 0, M3 = 0, M4 = 0, M5 = 0;
    private static boolean couponIssueStatus[] = new boolean[5];
    private static boolean hasStepCounter;

    private Sensor accel;
    private SensorManager sensorManager;

    private AccelStepDetector mAccelStepDetector;
    private CouponDbHandler dbHandler = new CouponDbHandler(this);


    private final IBinder mBinder = new StepBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    public class StepBinder extends Binder {
        public StepListener getService() {
            // Return this instance of LocalService so clients can call public methods
            return StepListener.this;
        }
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Check for false values

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mAccelStepDetector.updateAccel(
                    sensorEvent.timestamp, sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        }

        else if(sensorEvent.values[0] > Integer.MAX_VALUE)
        {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "probably a wrong value " + sensorEvent.values[0]);
            return;
        }

        else
        {
            //steps = (int) sensorEvent.values[0];
            if (BuildConfig.DEBUG)
                Log.d(TAG, "no of steps received " + steps);

            steps++;
            Toast.makeText(this,"steps = " + steps, Toast.LENGTH_SHORT).show();
            //publishSteps();
            //updateSteps(steps);
            sendCouponsAccordingToMilestones(steps);

        }
    }

    // obtains the time at which the step was detected by accelerometer
    @Override
    public void step(long timeNs) {
        steps++;
        Log.d(TAG, "no of steps received by accelerometer " + steps);
        sendCouponsAccordingToMilestones(steps);
    }

    private String getMallId(){

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                Context.MODE_PRIVATE);
        String mallId = sharedPreferences.getString(Constants.SharedPreferences.MALL_ID, "MH_0253_CCM");
        return mallId;
    }

    private void sendCouponsAccordingToMilestones(int steps) {
        Log.d(TAG, "sendCouponsAccordingToMilestones");
        Log.d(TAG, "steps = " + steps);
        Coupon coupon = null;
        if(M1 == 0 && M2 == 0){
            Log.d(TAG, "m1 and m2 are 0");
            return;
        }
        if(steps >= M1 && couponIssueStatus[0] == false){
            // send out first coupon as notification

            if(couponsToOffer.size() >= 1) {
                Log.d(TAG, "coupons to offer size = " + couponsToOffer.size());
                coupon = couponsToOffer.get(0);
                Log.d(TAG, "sending first coupon to user and first milestone achieved");
                 //sendCouponNotification(coupon);
                couponIssueStatus[0] = true;

            }
        }
        else if(steps >= M2 && couponIssueStatus[1] == false){
            // send out first coupon as notification

            if(couponsToOffer.size() >= 2) {
                Log.d(TAG, "coupons to offer size = " + couponsToOffer.size());
                coupon = couponsToOffer.get(1);
                Log.d(TAG, "sending second coupon to user and first milestone achieved");
                //sendCouponNotification(coupon);
                couponIssueStatus[1] = true;
            }
        }

        else if(steps >= M3 && couponIssueStatus[2] == false){
            // send out first coupon as notification

            if(couponsToOffer.size() >= 3) {
                Log.d(TAG, "coupons to offer size = " + couponsToOffer.size());
                coupon = couponsToOffer.get(2);
                Log.d(TAG, "sending third coupon to user and third milestone achieved");
                //sendCouponNotification(coupon);
                couponIssueStatus[2] = true;
            }
        }
        else if(steps >= M4 && couponIssueStatus[3] == false){
            // send out first coupon as notification

            if(couponsToOffer.size() >= 4) {
                Log.d(TAG, "coupons to offer size = " + couponsToOffer.size());
                coupon = couponsToOffer.get(3);
                Log.d(TAG, "sending fourth coupon to user and first milestone achieved");
                //sendCouponNotification(coupon);
                couponIssueStatus[3] = true;
            }
        }
        else if(steps >= M5 && couponIssueStatus[4] == false){
            // send out first coupon as notification

            if(couponsToOffer.size() >= 5) {
                Log.d(TAG, "coupons to offer size = " + couponsToOffer.size());
                coupon = couponsToOffer.get(4);
                Log.d(TAG, "sending fifth coupon to user and first milestone achieved");
                //sendCouponNotification(coupon);
                couponIssueStatus[4] = true;
            }
        }


        if(coupon != null){
            Log.d(TAG, "adding coupon id = " + coupon.getCouponId() + " to the allottedCouponDb");

            NotificationUtils.sendCustomCouponNotification(this, coupon);
            dbHandler.addCouponToAllottedList(coupon, getMallId());

            String timeOfAllotment = Calendar.getInstance().getTimeInMillis() + "";
            FirebaseUtils.addCouponAllotted(coupon.getCouponId(), timeOfAllotment);

            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent().setAction(Constants.Broadcast.COUPON_ALLOT));
        }
    }

    /*private void updateSteps(int steps) {
        SharedPreferences sharedPref = getSharedPreferences(Constants
                .SharedPreferences.STEPS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.SharedPreferences.STEPS, steps);
        editor.commit();
    }*/

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate of StepListener");
        super.onCreate();
        if(BuildConfig.DEBUG)
            Log.d(TAG, "StepListener Oncreate");

        hasStepCounter = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);

        // Initialize accelerometer step Detector if stepCounter is not found
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(!hasStepCounter){
            accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mAccelStepDetector = new AccelStepDetector();
            mAccelStepDetector.registerListener(this);
        }

        // prevent service from stopping when the phone goes to deep sleep in api >= 23
        ignoreDozeOptimization();

        // SharedPreferences sharedPref = getSharedPreferences(Constants.SharedPreferences.STEPS_FILE, Context.MODE_PRIVATE);
        steps = 0;      // Whenever the service is created it starts off with 0 steps.

        if(BuildConfig.DEBUG)
            Log.d(TAG, "steps from sharedPref " + steps);

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();

        if(BuildConfig.DEBUG)
            Log.d(TAG, "acquired partial wakelock");

        notificationBuilder = new NotificationCompat.Builder(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(dataReadyReceiver,
                new IntentFilter(Constants.Broadcast.MALL_DATA_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(dataReadyReceiver,
                new IntentFilter(Constants.Broadcast.VISITOR_DATA_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(userMallEntryConfirmReceiver,
                new IntentFilter(Constants.Geofence.SHOW_EARNING_ICON));

    }

    @Override
    public void onDestroy()
    {

        if(BuildConfig.DEBUG)
            Log.d(TAG, "StepListener onDestroy");
        unRegisterSensor();
        releaseWakeLock();

        super.onDestroy();

    }

    private BroadcastReceiver dataReadyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //TODO: Add a tag to the mall ready broadcasts so that it does not refreshes when not
            // intended to

            Log.d(TAG, "received DATA READY broadcast");
            Log.d(TAG, "received the intent action = " +  intent.getAction());
            if(intent.getAction().equals(Constants.Broadcast.MALL_DATA_READY))
            {
                Log.d(TAG, "received the mall data");
                // get the 5 coupons from the coupon list according to visitor number
                Mall mall = FirebaseUtils.sMall;
                Log.d(TAG, "mall name received = " + mall.getName());
                int no_of_coupons_available = mall.getCoupons().size();
                int starting_index = 0;
                if(visitor_no != 0){
                    starting_index = (int)(visitor_no*no_of_coupons_to_give)% no_of_coupons_available;
                }

                ArrayList<Coupon> allCouponsFromMallList = new ArrayList<>();
                for(Map.Entry<String, Coupon> entry: mall.getCoupons().entrySet()) {
                    allCouponsFromMallList.add(entry.getValue());
                }

                for(int i = 1; i <= no_of_coupons_to_give; i++){
                    // store the required coupons in an array
                    Log.d(TAG, "index of coupon stored = " + (starting_index++)%no_of_coupons_to_give);
                    Log.d(TAG, "coupon brand added = " + allCouponsFromMallList.get((starting_index++)%no_of_coupons_to_give).getBrand());
                    couponsToOffer.add(allCouponsFromMallList.get((starting_index++)%no_of_coupons_to_give));
                }

                // get the milestones
                M1 = mall.getM1();
                M2 = mall.getM2();
                M3 = mall.getM3();
                M4 = mall.getM4();
                M5 = mall.getM5();
                Log.d(TAG, "milestone 1 = " + M1);

            }
            else if(intent.getAction().equals(Constants.Broadcast.VISITOR_DATA_READY)){
                Log.d(TAG, "received visitor number");
                // Grab hold of the coupons according to the number
                // Gets the visitor number, updates it and sends the coupons to the step listener service
                visitor_no = FirebaseUtils.visitor_number;
                Log.d(TAG, "visitor no = " + visitor_no);

                // get the mall object
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                        Context.MODE_PRIVATE);
                String mallId = sharedPreferences.getString(Constants.SharedPreferences.MALL_ID, "MH_0253_CCM");
                FirebaseUtils.getMall(StepListener.this, mallId);

                // increase the visitor number by 1
                FirebaseUtils.updateVisitorNumber(OfferSkyUtils.getCurrentMallId(StepListener.this));
                // handle rest of code in get mall part of data ready broadcast ie get the coupons
            }

        }
    };

    private BroadcastReceiver userMallEntryConfirmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received userMallEntryConfirm Broadcast");
            // TODO: IMPORTANT Check for active internet connection before proceeding
            // Also check if active running net connection ensures retrieval of the latest data
            // Get the visitor number
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                    Context.MODE_PRIVATE);
            String mallId = sharedPreferences.getString(Constants.SharedPreferences.MALL_ID, "MH_0253_CCM");
            FirebaseUtils.getVisitorNumber(StepListener.this, mallId);
            // Update the visitor number if it is valid
            // now the rest of the code is handled in the visitorNumberReadyBroadcast implementation
        }
    };

    public void unRegisterSensor()
    {
        if(BuildConfig.DEBUG)
            Log.d(TAG, "un-register sensor listener");
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(hasStepCounter && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try{
                sm.unregisterListener(this);
            } catch (Exception e){
                if(BuildConfig.DEBUG) Log.d(TAG, "error in un registering sensor listener");
                e.printStackTrace();
            }
        }

        else{
            sensorManager.unregisterListener(this);
        }

    }

    public void reRegisterSensor()
    {
        if(BuildConfig.DEBUG)
            Log.d(TAG, "re-register sensor listener");

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        unRegisterSensor();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "step sensors: " + sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size());
            if (sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size() < 1) return; // emulator
            Log.d(TAG, "default: " + sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER).getName());
        }

        if(hasStepCounter && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                    SensorManager.SENSOR_DELAY_NORMAL, 1000);
        }

        // Use accelerometer to count steps
        else{
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        }



    }


    public static void releaseWakeLock()
    {
        if(wakeLock != null) {
            if(wakeLock.isHeld()) {     //required to avoid crash, sometimes wakelock is not held and
                Log.d(TAG, "releasing wakelock");                  // the internal reference counter goes negative causing the crash
                wakeLock.release();
            }
        }
    }


    /*public void sendCouponNotification(Coupon coupon){

        Log.i(TAG, "sendCouponNotification: " + coupon.getBrand());

        Intent notificationIntent = new Intent(this, MainActivity.class);


        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Creating and sending Notification
        NotificationManager notificationMng =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );


        NotificationCompat.Builder notificationBuilder = createNotification(coupon.getBrand(), coupon.getDescription(), notificationPendingIntent);

        // TODO: Important: Create notification with the coupon image
       try{
           Bitmap bigCouponPic = Picasso.with(this).load(coupon.getCouponImageURL()).get();
           notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigCouponPic));
       } catch(Exception e){
            Log.e(TAG, e.toString() + " error setting coupon picture");
       }

        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle());
        final int notifId = coupon.hashCode();
        final int bigIconId = getResources().getIdentifier("android:id/big_picture", null, null);

        //Target t1 = new Target()

        notificationMng.notify(
                coupon.hashCode(),
                notificationBuilder.build());
    }*/

    public void sendEarningNotification(String title, String msg ) {
        Log.i(TAG, "sendEarningNotification: " + msg );

        int notification_id = 1337;
        Notification notification = NotificationUtils.getEarningNotification(this, title, msg, notification_id);
        startForeground(notification_id, notification);


        /*// Intent to start the main Activity
        Intent notificationIntent = new Intent(this, MainActivity.class);


        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Creating and sending Notification
        NotificationManager notificationMng =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );

        Notification notification = createNotification(title, msg, notificationPendingIntent).build();
        notificationMng.notify(
                1337,
                notification);

        startForeground(1337, notification);        //to keep the service running even after power off*/

    }

    /*private NotificationCompat.Builder createNotification(String title, String msg, PendingIntent notificationPendingIntent) {

        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.RED)
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true);
        return notificationBuilder;
    }*/

    private void ignoreDozeOptimization() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        String packageName = getPackageName();
        if (Build.VERSION.SDK_INT >= 23 && !pm.isIgnoringBatteryOptimizations(packageName)) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
        }
    }
}
