package com.example.abhiraj.offersky.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.DbHandler.CouponDbHandler;
import com.example.abhiraj.offersky.model.Coupon;
import com.example.abhiraj.offersky.model.Mall;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Abhiraj on 18-06-2017.
 */

public class CouponUtils {

    private static final String TAG = CouponUtils.class.getSimpleName();

    private static List<Coupon> validAllotableCoupons;
    // Function to test the validity of the coupon before allotting to the user
    public static boolean isCouponAllotable(Context context, Coupon coupon){

        //TODO: Add expiry date to the coupons
        // We have an integer number denoting number of hours for which the coupon is valid
        // after allotting it to the user
        int validity = coupon.getValidity();        // integer value specifying hours for which coupon is valid
        Log.d(TAG, "validity of coupon " + coupon.getBrand() + " is  = " + validity);
        CouponDbHandler db = new CouponDbHandler(context);

        String allotment_time_str = db.getCouponAllotmentEpochTime(coupon);
        //Log.d(TAG, "allotment time of coupon is "  + allotment_time_str);

        String curr_time_str = Calendar.getInstance().getTimeInMillis() + "";

        //Log.d(TAG, "curr time in millis = " + curr_time_str);

        long allotment_time = Long.parseLong(allotment_time_str);
        long curr_time = Long.parseLong(curr_time_str);
        long diff = curr_time - allotment_time;

        long diff_hour = diff / (60 * 60 * 1000);
        Log.d(TAG, "diffHour = " + diff_hour);

        boolean isAllotable = false;
        if(diff_hour < validity){
            isAllotable = true;
        }
        return isAllotable;
    }

    public static long getCouponAllottmentTime(Context context, Coupon coupon){
        Log.d(TAG, "getCoupnAllottmentTime(" + coupon.getCouponId() + ")");

        CouponDbHandler db = new CouponDbHandler(context);

        String allotment_time_str = db.getCouponAllotmentEpochTime(coupon);
        long allotment_time = Long.parseLong(allotment_time_str);
        return allotment_time;
    }

    public static int isCouponRedeemed(Context context, Coupon coupon){

        Log.d(TAG, "isCouponRedeemed(" + coupon.getCouponId() + " )");

        CouponDbHandler db = new CouponDbHandler(context);
        String redeem_status = db.getRedeemStatus(coupon);
        // 0 implies not redeemed
        if(redeem_status.equals("0"))
            return 0;
        // anything else is the time in millis when the coupon was redeemed
        return 1;
    }

    public static void setCouponAsRedeemed(Context context, Coupon coupon){
        Log.d(TAG, "setCouponAsRedeemed(" + coupon.getCouponId() + " )");

        CouponDbHandler db = new CouponDbHandler(context);
        db.setRedeemStatus(coupon);
    }

    public static List<Coupon> getAllottableCoupons(Context context, String mallId){
        Log.d(TAG, "getAllottableCoupons(" + mallId + ")");

        validAllotableCoupons = new ArrayList<>();
        List<String> allottedCouponIds;
        CouponDbHandler db = new CouponDbHandler(context);
        allottedCouponIds = db.getAllAllottedCouponIds(mallId);

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                Context.MODE_PRIVATE);

        try{
            String mall_string = sharedPreferences.getString(Constants.SharedPreferences.MALL_JSON, "");
            Gson mall_gson = new Gson();
            Mall mall = mall_gson.fromJson(mall_string, Mall.class);
            Map<String, Coupon> couponMap = mall.getCoupons();
            for(String allottedCouponId : allottedCouponIds){
                Coupon coupon = couponMap.get(allottedCouponId);
                if(coupon != null){
                    // check if the coupon is still valid
                    if(CouponUtils.isCouponAllotable(context, coupon)){
                        validAllotableCoupons.add(coupon);
                    }
                    else{
                        db.deleteCouponFromAllottedList(coupon, mallId);
                    }
                }
            }

        } catch (Exception e){
            Log.e(TAG, e.toString());
            Log.e(TAG, "error in obtaining mall maybe from json?");
        }
        return validAllotableCoupons;
    }

    public static int getAllotableCouponCount(Context context, String mallId){
        return getAllottableCoupons(context, mallId).size();
    }

    public static int noOfValidCouponsAllotted(Context context){
        Log.d(TAG, "noOfValidCouponsAllotted()");

        // returns the no of coupons currently valid and allotted to the user
        // in the currently selected mall

        // get the count of coupons in the current mall
        return 0;
    }
    // Function to add coupons given to the user to shared preferences along with the date and
    // time of allotment
}
