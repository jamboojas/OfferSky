package com.example.abhiraj.offersky.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.viewholder.CouponViewHolder;
import com.example.abhiraj.offersky.model.Coupon;
import com.example.abhiraj.offersky.utils.CouponUtils;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Abhiraj on 24-04-2017.
 */

public class CouponAdapter extends SortedListAdapter<Coupon> {

    private static final String TAG = CouponAdapter.class.getSimpleName();

    private Context mContext;
    private static List<Coupon> mCoupons;

    private final int REDEEMED_COUPON_TYPE = 1;
    private final int NOT_REDEEMED_COUPON_TYPE = 0;
    private CouponClickListener mCouponClickListener;

    public CouponAdapter(Context context, Class<Coupon> itemClass, Comparator<Coupon> comparator,
                         CouponClickListener couponClickListener) {
        super(context, itemClass, comparator);
        mContext = context;
        mCouponClickListener = couponClickListener;
    }

    /*public CouponAdapter(Context context, List<Coupon> coupons, CouponClickListener couponCodeClickListener){
        mContext = context;
        mCoupons = coupons;
        mCouponClickListener = couponCodeClickListener;

    }*/

    @Override
    public int getItemViewType(int position) {
        Coupon coupon = getItem(position);

        return CouponUtils.isCouponRedeemed(mContext, coupon);
    }


    @Override
    public CouponViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {

        CouponViewHolder viewHolder;
        switch (viewType)
        {
            case REDEEMED_COUPON_TYPE:
                Log.d(TAG, "Redeemed Coupon view holder created");
                View view1 = inflater.inflate(R.layout.coupon_card, parent,false);
                viewHolder = new CouponViewHolder(view1, mCouponClickListener);
                break;
            case NOT_REDEEMED_COUPON_TYPE:
                Log.d(TAG, "Not Redeemed Coupon view holder created");
                View view2 = inflater.inflate(R.layout.new_coupon_card, parent,false);
                viewHolder = new CouponViewHolder(view2, mCouponClickListener);
                break;

            default:
                View viewDef = inflater.inflate(R.layout.coupon_card, parent,false);
                viewHolder = new CouponViewHolder(viewDef, mCouponClickListener);
                break;
        }

        return viewHolder;
    }

    @Override
    protected boolean areItemsTheSame(Coupon coupon, Coupon t1) {

        if(coupon == t1){
            return true;
        }
        return coupon.getCouponId().equals(t1.getCouponId());
    }

    @Override
    protected boolean areItemContentsTheSame(Coupon coupon, Coupon t1) {
        return coupon.equals(t1);
    }

    /*@Override
    public void onBindViewHolder(CouponViewHolder holder, int position) {
            holder.bindViews(mCoupons.get(position));
    }

    @Override
    public int getItemCount() {
        return mCoupons.size();
    }*/



    /*public class CouponViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final String TAG = EventViewHolder.class.getSimpleName();

        private View mView;
        private CouponAdapter.CouponClickListener mCouponClickListener;

        @BindView(R.id.iv_coupon)
        ImageView coupon_iv;
        @BindView(R.id.tv_coupon)
        TextView coupon_tv;
        @BindView(R.id.btn_get_code)
        Button code_btn;
        public CouponViewHolder(View itemView, CouponAdapter.CouponClickListener couponCodeClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
            mCouponClickListener = couponCodeClickListener;

            code_btn.setOnClickListener(this);
        }


        // TODO: Add listener for get code button for coupon
        public void bindViews(Coupon coupon){

            Picasso.with(mView.getContext())
                    .load(coupon.getCouponImageURL())
                    .into(coupon_iv);

            coupon_tv.setText(coupon.getDescription());
        }

        @Override
        public void onClick(View view) {

            if(view == code_btn){
                // Call the code click implementation
                Coupon coupon = mCoupons.get(getAdapterPosition());
                mCouponClickListener.onCouponClick(coupon);
            }
        }
    }*/

    public interface CouponClickListener {

        void onCouponClick(Coupon coupon);

    }
}
