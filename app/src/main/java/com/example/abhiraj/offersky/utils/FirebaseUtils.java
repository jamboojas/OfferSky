package com.example.abhiraj.offersky.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.model.Mall;
import com.example.abhiraj.offersky.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.joda.time.DateTime;

/**
 * Created by Abhiraj on 17-04-2017.
 */

public class FirebaseUtils {

    private static final String TAG = FirebaseUtils.class.getSimpleName();

    public static Mall sMall;
    public static long visitor_number = -2;

    private static String FIREBASE_MALL_TAG = "malls";
    private static String FIREBASE_VISITOR_TAG = "visitors";
    private static String FIREBASE_USER_TAG = "users";
    private static String FIREBASE_USER_DETAILS = "details";
    private static String FIREBASE_USER_NAME = "name";
    private static String FIREBASE_USER_EMAIL = "email";
    private static String FIREBASE_VISITOR_NO_TAG = "vno";
    private static String FIREBASE_SUCCESSFUL_MALL_VISIT = "successful_mall_visit";
    private static String FIREBASE_UNSUCCESSFUL_MALL_VISIT = "unsuccessful_mall_visit";
    private static String FIREBASE_COUPON_ALLOT_TIME = "allotted_coupons";
    private static String FIREBASE_COUPON_REDEEM_TIME = "redeemed_coupons";

    public static void getMall(final Context context, String mall_id){

        Log.d(TAG, mall_id);
        String uid;

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        // TODO: Add firebase user access;

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference(FIREBASE_MALL_TAG
         + "/" + mall_id);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onMallDataChange()");

                Log.d(TAG, dataSnapshot.getKey());
                Log.d(TAG, dataSnapshot.getChildrenCount()+ "");

                try {
                    sMall = dataSnapshot.getValue(Mall.class);
                    Gson mall_gson = new Gson();
                    String mall_json = mall_gson.toJson(sMall);
                    editor.putString(Constants.SharedPreferences.MALL_JSON, mall_json);
                    editor.commit();
                }
                catch (Exception e){
                    Log.e(TAG, e.toString());
                }
                if(sMall != null) {
                    sendDataReadyBroadcast(context, 0);     // 0 for mall data ready
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void addUser(User user){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Log.d(TAG, firebaseUser.getUid() + " firebase user id");
            mRef.child(FIREBASE_USER_TAG).child(firebaseUser.getUid()).child(FIREBASE_USER_DETAILS).setValue(user);
        }

    }

    public static void addMallVisit(String mallId, String timeOfEntry, boolean isEntrySuccessful){
        Log.d(TAG, "addMallVisit("+ mallId + ", " + timeOfEntry);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            if(isEntrySuccessful) {
                mRef.child(FIREBASE_USER_TAG).child(firebaseUser.getUid()).child(FIREBASE_SUCCESSFUL_MALL_VISIT)
                        .child(timeOfEntry).setValue(mallId);
            }
            else {
                mRef.child(FIREBASE_USER_TAG).child(firebaseUser.getUid()).child(FIREBASE_UNSUCCESSFUL_MALL_VISIT)
                        .child(timeOfEntry).setValue(mallId);
            }
        }
    }

    public static void addCouponAllotted(String couponId, String timeOfAllottment){
        Log.d(TAG, "addCouponAllotted(" + couponId + ", " + timeOfAllottment);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            mRef.child(FIREBASE_USER_TAG).child(firebaseUser.getUid()).child(FIREBASE_COUPON_ALLOT_TIME)
                    .child(timeOfAllottment).setValue(couponId);
        }
    }

    public static void addCouponRedeemed(String couponId, String timeOfRedeem){
        Log.d(TAG, "addCouponRedeemed(" + couponId + ", " + timeOfRedeem);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            mRef.child(FIREBASE_USER_TAG).child(firebaseUser.getUid()).child(FIREBASE_COUPON_REDEEM_TIME)
                    .child(timeOfRedeem).setValue(couponId);
        }
    }

    public static void getVisitorNumber(final Context context, String mall_id){

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference(FIREBASE_VISITOR_TAG
                + "/" + mall_id + "/" + FIREBASE_VISITOR_NO_TAG);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "visitor data change encountered");
                //dataSnapshot.getValue();
                try {
                    visitor_number = (long) dataSnapshot.getValue();
                    Log.d(TAG, "visitor no obtained = " + visitor_number);
                }
                catch (Exception e){
                    Log.d(TAG, e.toString());
                }
                // TODO: Seems like a redundant check, test it and remove if unnecessary

                if(visitor_number != -2){
                    sendDataReadyBroadcast(context, 1);             // 1 for visitor number ready
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void updateVisitorNumber(String mall_id){
        // Increases visitor number by 1 for the obtained mall id
        Log.d(TAG, "updateVisitorNumber()");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference(FIREBASE_VISITOR_TAG
                + "/" + mall_id + "/" + FIREBASE_VISITOR_NO_TAG);

        mRef.runTransaction(new Transaction.Handler(){

            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                try {
                    long noOfVisitors = (long) mutableData.getValue();
                    noOfVisitors = noOfVisitors + 1;
                    mutableData.setValue(noOfVisitors);
                } catch (Exception e){
                    Log.e(TAG, "error in obtaining visitor no when updating" + e.toString());
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    private static void udpateUserEntryRecord(Context context, String mall_id){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference(FIREBASE_USER_TAG);
            mRef.child(user.getUid()).push().child(mall_id).child("in_time").setValue(DateTime.now());
        }
    }

    private static void sendDataReadyBroadcast(Context context, int type) {

        Intent intent = new Intent();
        switch (type){
            case 0:
                Log.d(TAG, "sending mall data ready broadcast");
                intent.setAction(Constants.Broadcast.MALL_DATA_READY);
                break;
            case 1:
                Log.d(TAG, "sending visitor data ready broadcast");
                intent.setAction(Constants.Broadcast.VISITOR_DATA_READY);
                break;
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
