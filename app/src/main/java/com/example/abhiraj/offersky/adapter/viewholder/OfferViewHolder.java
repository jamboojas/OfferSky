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
 * Created by Abhiraj on 18-04-2017.
 */

public class OfferViewHolder  extends RecyclerView.ViewHolder{

    private static final String TAG = OfferViewHolder.class.getSimpleName();
    private View mView;

    @BindView(R.id.iv_offer_image)
    ImageView offer_image_iv;
    public OfferViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public void bindViews(String offer_url, int position)
    {
        Picasso.with(mView.getContext())
                .load(offer_url)
                .placeholder(R.drawable.progress_animation)
                .into(offer_image_iv);
        Log.d(TAG, "offer at position " + position + " bound");
    }
}
