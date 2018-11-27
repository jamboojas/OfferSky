package com.example.abhiraj.offersky.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.DbHandler.CouponDbHandler;
import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.CouponAdapter;
import com.example.abhiraj.offersky.fragment.CouponBottomSheetFragment;
import com.example.abhiraj.offersky.model.Coupon;
import com.example.abhiraj.offersky.utils.CouponUtils;
import com.example.abhiraj.offersky.utils.OfferSkyUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CouponActivity extends AppCompatActivity  implements CouponAdapter.CouponClickListener {

    private static final String TAG = CouponActivity.class.getSimpleName();

    private CouponAdapter mCouponAdapter;
    private List<Coupon> mModels;
    private List<String> allottedCouponIds;
    private CouponDbHandler db;

    @BindView(R.id.rv_coupon)RecyclerView coupon_rv;
    @BindView(R.id.empty_view)TextView empty_msg_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        ButterKnife.bind(this);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Coupons");
            db = new CouponDbHandler(this);
            setupRecyclerView();
        }

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(couponAllottedReceiver, new IntentFilter(Constants.Broadcast.COUPON_ALLOT));
    }

    @Override
    protected void onResume() {
        refreshCoupons();
        super.onResume();
    }

    private void refreshCoupons(){
        Log.d(TAG, "refreshCoupons()");

        String mallId = OfferSkyUtils.getCurrentMallId(this);
        mModels = CouponUtils.getAllottableCoupons(this, mallId);
        showEmptyIfNoCoupon();
        mCouponAdapter.edit().replaceAll(mModels).commit();
    }
    private void setupRecyclerView() {

        Log.d(TAG, "insetupRecyclerView()");
        if(coupon_rv != null){
            Log.d(TAG, "setupRecyclerView()");
            mModels = new ArrayList<>();

            final Comparator<Coupon> COUPON_REDEEM_COMPARATOR = new Comparator<Coupon>() {
                @Override
                public int compare(Coupon a, Coupon b) {
                    String a_redeem_time = db.getRedeemStatus(a);
                    String b_redeem_time = db.getRedeemStatus(b);
                    return a_redeem_time.compareTo(b_redeem_time);
                }
            };


            // get all the coupons from the local coupon database
            String mallId = OfferSkyUtils.getCurrentMallId(this);
            mModels = CouponUtils.getAllottableCoupons(this, mallId);

            /*allottedCouponIds = db.getAllAllottedCouponIds(mallId);

            if(allottedCouponIds.size() == 0)
            {
                Log.d(TAG, "No coupons have been allotted yet");
                coupon_rv.setVisibility(View.GONE);
                empty_msg_tv.setVisibility(View.VISIBLE);
            }

            else{
                // grab hold of the mall object and get all the coupons which have been allotted
                // and store them in mModels
                try{
                    Mall mall = FirebaseUtils.sMall;
                    Map<String, Coupon> couponMap = mall.getCoupons();
                    for(String allottedCouponId : allottedCouponIds){
                        Coupon coupon = couponMap.get(allottedCouponId);
                        if(coupon != null){
                            // check if the coupon is still valid
                            if(CouponUtils.isCouponAllotable(this, coupon)){
                                mModels.add(coupon);
                            }
                            else{
                                db.deleteCouponFromAllottedList(coupon, mallId);
                            }
                        }
                    }

                } catch (Exception e){
                    Log.e(TAG, e.toString());
                    Log.e(TAG, "error in obtaining mall maybe?");
                }
            }*/
            Log.d(TAG, "no of coupons = " + mModels.size());
            showEmptyIfNoCoupon();
            mCouponAdapter = new CouponAdapter(this, Coupon.class, COUPON_REDEEM_COMPARATOR, this);
            coupon_rv.setLayoutManager(new LinearLayoutManager(this));
            coupon_rv.setAdapter(mCouponAdapter);
            mCouponAdapter.edit().replaceAll(mModels).commit();

        }
    }

    BroadcastReceiver couponAllottedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshCoupons();
        }
    };

    @Override
    public void onCouponClick(final Coupon coupon) {
        Log.d(TAG, "Coupon clicked is = " + coupon.getBrand());

        // TODO: Temporarily removed the code dialog to test the botttom sheet
        /*new AlertDialog.Builder(this)
                .setTitle("Code")
                .setMessage("The coupon code is " + coupon.getCode())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        // Redeem the coupon if it is not already redeemed
                        int redeemStatus = CouponUtils.isCouponRedeemed(CouponActivity.this, coupon);
                        if(redeemStatus == 0) {
                            CouponUtils.setCouponAsRedeemed(CouponActivity.this, coupon);
                            String timeofRedeem = Calendar.getInstance().getTimeInMillis() + "";
                            FirebaseUtils.addCouponRedeemed(coupon.getCouponId(), timeofRedeem);
                        }
                    }
                }).show();*/

        CouponBottomSheetFragment couponBottomSheetFragment = CouponBottomSheetFragment.newInstance();
        couponBottomSheetFragment.setCoupon(coupon);
        couponBottomSheetFragment.show(getSupportFragmentManager(), couponBottomSheetFragment.getTag());

    }

    private void showEmptyIfNoCoupon(){
        if(mModels.size() == 0){
            coupon_rv.setVisibility(View.GONE);
            empty_msg_tv.setVisibility(View.VISIBLE);
        }
        else{
            coupon_rv.setVisibility(View.VISIBLE);
            empty_msg_tv.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}
