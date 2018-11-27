package com.example.abhiraj.offersky.DbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.abhiraj.offersky.model.Coupon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Abhiraj on 19-06-2017.
 */

public class CouponDbHandler extends SQLiteOpenHelper {


    private static final String TAG = CouponDbHandler.class.getSimpleName();
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "CouponDb";

    // Coupon table name
    private static final String TABLE_COUPONS = "allottedCoupons";

    // Coupon table column names
    private static final String KEY_ID = "couponId";
    private static final String KEY_MALL_ID = "mallId";
    private static final String KEY_ALLOTMENT_DATE = "allotmentDate";
    private static final String KEY_ALLOTMENT_TIME = "allotmentTime";
    private static final String KEY_ALLOTMENT_EPOCH_TIME = "allotment_epoch_time";
    private static final String KEY_REDEEM_STATUS = "coupon_redeem_status";

    private static final String NOT_REDEEM_VALUE = "0";
    public CouponDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COUPONS_TABLE = "CREATE TABLE " + TABLE_COUPONS + "("
                + KEY_ID + " VARCHAR(50) PRIMARY KEY," + KEY_MALL_ID + " VARCHAR(100),"
                + KEY_ALLOTMENT_DATE + " VARCHAR(20)," + KEY_ALLOTMENT_TIME + " VARCHAR(20), " +
                KEY_ALLOTMENT_EPOCH_TIME + " VARCHAR(20), " + KEY_REDEEM_STATUS + " VARCHAR(20) "+ ")";
        db.execSQL(CREATE_COUPONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPONS);

        // Create tables again
        onCreate(db);
    }

    // Adding new coupon entry to allotted coupons
    public void addCouponToAllottedList(Coupon coupon, String mallId) {

        Log.d(TAG, "addCouponToAllottedList() " + coupon.getCouponId() + " to mall = " + mallId);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, coupon.getCouponId());
        values.put(KEY_MALL_ID, mallId);

        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        values.put(KEY_ALLOTMENT_DATE, date);

        String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        values.put(KEY_ALLOTMENT_TIME, time);

        String epoch_time = Calendar.getInstance().getTimeInMillis() + "";
        values.put(KEY_ALLOTMENT_EPOCH_TIME, epoch_time);

        values.put(KEY_REDEEM_STATUS, NOT_REDEEM_VALUE);

        Log.d(TAG, "epoch time is = " + epoch_time);
        Log.d("TIME", time + " is the time obtained");
        Log.d("DATE", date + " is the date obtained");

        // Inserting Row
        db.insert(TABLE_COUPONS, null, values);
        db.close(); // Closing database connection
    }


    // Delete coupons from allotted list
    public void deleteCouponFromAllottedList(Coupon coupon, String mallId){

        Log.d(TAG, "deleteCouponFromAllottedList()"  + coupon.getCouponId());
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_COUPONS + " WHERE " + KEY_ID + " = '" + coupon.getCouponId() + "'";
        db.delete(TABLE_COUPONS, KEY_ID + "='" + coupon.getCouponId() + "'", null);
        //db.rawQuery(query, null);
        db.close();
    }

    public String getRedeemStatus(Coupon coupon){
        // Returns 0 if the coupon has not been redeemed
        // else returns the time in milliseconds since epoch when the coupon was redeemed
        Log.d(TAG, "getRedeemStatus(" + coupon.getCouponId() + ")");

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_COUPONS + " WHERE " + KEY_ID + " = '"  + coupon.getCouponId() + "'";
        Cursor cursor = db.rawQuery(query, null);

        String redeem_status = "0";

        if(cursor != null){
            cursor.moveToFirst();

            redeem_status = cursor.getString(cursor.getColumnIndex(KEY_REDEEM_STATUS));
        }
        Log.d(TAG, "redeem status = " + redeem_status);
        cursor.close();
        db.close();
        return redeem_status;
    }

    public void setRedeemStatus(Coupon coupon){
        Log.d(TAG, "setRedeemStatus(" + coupon.getCouponId() + ")");

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues cv = new ContentValues();
        String epoch_time = Calendar.getInstance().getTimeInMillis() + "";
        cv.put(KEY_REDEEM_STATUS, epoch_time);   // time when the coupon was redeemed in millis

        db.update(TABLE_COUPONS, cv, KEY_ID + "= '" + coupon.getCouponId() + "'", null);

        /*String query = "UPDATE " + TABLE_COUPONS + " SET " + KEY_REDEEM_STATUS + " = " + REDEEM_VALUE
                + " WHERE " + KEY_ID  + " = '" + coupon.getCouponId() + "'";
        db.rawQuery(query, null);*/

        db.close();
    }

    // Get all Coupons allotted to the user from a mall
    public List<String> getAllAllottedCouponIds(String mallId){
        Log.d(TAG, "getAllAllottedCouponIds() from " + mallId);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUPONS, new String[] { KEY_ID}, KEY_MALL_ID + "=?",
                new String[] { String.valueOf(mallId) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        List<String> allottedCouponIds = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                Log.d("getIDs " , "coupon id received = " + cursor.getString(0));
                allottedCouponIds.add(cursor.getString(0));

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return allottedCouponIds;
    }

    public String getCouponAllotmentEpochTime(Coupon coupon){
        Log.d(TAG, "getCouponAllotmentDate() " + coupon.getCouponId());
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_COUPONS + " WHERE " +
                KEY_ID + " = '" + coupon.getCouponId()+ "'";

        Cursor cursor = db.rawQuery(query, null);

        String allotmentDate = null;
        if(cursor != null){
            cursor.moveToFirst();
            allotmentDate = cursor.getString(cursor.getColumnIndex(KEY_ALLOTMENT_EPOCH_TIME));

        }
        cursor.close();
        db.close();
        return allotmentDate;
    }

    public String getCouponAllottmentDate(Coupon coupon){
        Log.d(TAG, "getCouponAllottmentDate() " + coupon.getCouponId());
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + KEY_ALLOTMENT_DATE + " FROM " + TABLE_COUPONS + " WHERE " +
                KEY_ID + " = " + coupon.getCouponId();

        Cursor cursor = db.rawQuery(query, null);

        String allottmentDate = null;
        if(cursor != null){
            cursor.moveToFirst();
            allottmentDate = cursor.getString(cursor.getColumnIndex(KEY_ALLOTMENT_DATE));

        }
        cursor.close();
        db.close();
        return allottmentDate;
    }

    public String getCouponAllottmentTime(Coupon coupon){
        Log.d(TAG, "getCouponAllottmentTime() " + coupon.getCouponId());
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_COUPONS + " WHERE " +
                KEY_ID + " = " + coupon.getCouponId();

        Cursor cursor = db.rawQuery(query, null);

        String allottmentTime = null;
        if(cursor != null){
            cursor.moveToFirst();
            allottmentTime = cursor.getString(cursor.getColumnIndex(KEY_ALLOTMENT_DATE));

        }
        cursor.close();
        db.close();
        return allottmentTime;
    }

    // Getting coupons Count
    public int getCouponsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COUPONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        Log.d("getCouponsCount()", cursor.getCount() + " ");
        // return count
        cursor.close();
        db.close();
        return cursor.getCount();
    }
}
