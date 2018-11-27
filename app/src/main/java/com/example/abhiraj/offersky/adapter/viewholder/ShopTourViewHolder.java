package com.example.abhiraj.offersky.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.abhiraj.offersky.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhiraj on 27-07-2017.
 */

public class ShopTourViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ShopTourViewHolder.class.getSimpleName();
    private View mView;

    @BindView(R.id.shop_tour_iv)ImageView shop_tour_iv;
    public ShopTourViewHolder(View itemView) {
        super(itemView);
        Log.d(TAG, "ShopTourViewHolder()");
        mView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void bindView(String shopTourPhotoURL){
        Log.d(TAG, "bindView()");
        Log.d(TAG, "loading .. "  + shopTourPhotoURL);
        Picasso.with(mView.getContext()).load(shopTourPhotoURL)
                .placeholder(R.drawable.badge_circle)
                .fit()
                .into(shop_tour_iv);
    }
}
