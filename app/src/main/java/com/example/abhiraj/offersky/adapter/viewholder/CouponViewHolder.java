package com.example.abhiraj.offersky.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.CouponAdapter;
import com.example.abhiraj.offersky.model.Coupon;
import com.example.abhiraj.offersky.utils.CouponUtils;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhiraj on 24-04-2017.
 */

public class CouponViewHolder extends SortedListAdapter.ViewHolder<Coupon> implements View.OnClickListener {

    private static final String TAG = CouponViewHolder.class.getSimpleName();

    private View mView;
    private CouponAdapter.CouponClickListener mCouponClickListener;
    private static final String FORMAT = "%02d:%02d:%02d";

    @BindView(R.id.iv_coupon)
    ImageView coupon_iv;
    @BindView(R.id.tv_coupon)
    TextView coupon_tv;
    /*@BindView(R.id.btn_get_code)
    Button code_btn;*/
    @BindView(R.id.tv_expiration_time)
    TextView expiration_tv;
    public CouponViewHolder(View itemView, CouponAdapter.CouponClickListener couponClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mView = itemView;
        mCouponClickListener = couponClickListener;

        //code_btn.setOnClickListener(this);
        mView.setOnClickListener(this);
    }


    // TODO: Add listener for get code button for coupon
    /*public void bindViews(Coupon coupon){

        Picasso.with(mView.getContext())
                .load(coupon.getCouponImageURL())
                .into(coupon_iv);

        coupon_tv.setText(coupon.getDescription());
    }*/

    @Override
    public void onClick(View view) {

        /*if(view == code_btn){
            // Call the code click implementation
            mCouponClickListener.onCouponClick(getCurrentItem());
        }*/
        if(view == mView){
            mCouponClickListener.onCouponClick(getCurrentItem());
        }
    }

    @Override
    protected void performBind(Coupon coupon) {
        Log.d(TAG, "performBind(" + coupon.getBrand() + ")");
        Picasso.with(mView.getContext())
                .load(coupon.getCouponImageURL())
                .into(coupon_iv);

        coupon_tv.setText(coupon.getDescription());

       /* // get the expiration time of coupon in milliseconds
        long coupon_validity_time = coupon.getValidity() * 60 * 60 * 1000;  // hours to milliseconds
        long current_time = Calendar.getInstance().getTimeInMillis();
        long coupon_allottment_time = CouponUtils.getCouponAllottmentTime(mView.getContext(), coupon);
        long coupon_expiration_time = coupon_allottment_time + coupon_validity_time - current_time;

        new CountDownTimer(coupon_expiration_time, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                expiration_tv.setText(""+String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                expiration_tv.setText("done!");
            }
        }.start();*/

       // Simpler method, shows only the hours remaining and updates only when refreshed
        long coupon_validity_time = coupon.getValidity() * 60 * 60 * 1000;  // hours to milliseconds
        long current_time = Calendar.getInstance().getTimeInMillis();
        long coupon_allottment_time = CouponUtils.getCouponAllottmentTime(mView.getContext(), coupon);
        long coupon_expiration_time = coupon_allottment_time + coupon_validity_time - current_time;

        int hours = (int) ((coupon_expiration_time / (1000 * 60 * 60)) % 24);

        expiration_tv.setText("Expires in : " + hours + " hours!");
    }
}
