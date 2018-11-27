package com.example.abhiraj.offersky.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.ShopAdapter;
import com.example.abhiraj.offersky.model.Offer;
import com.example.abhiraj.offersky.model.Shop;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhiraj on 14-04-2017.
 */

public class ShopViewHolder extends SortedListAdapter.ViewHolder<Shop> implements View.OnClickListener {

    private static final String TAG = ShopViewHolder.class.getSimpleName();
    private View mView;
    private ShopAdapter.ShopClickListener mShopClickListener;

    @BindView(R.id.tv_shop_name)
    TextView shop_name_tv;
    @BindView(R.id.tv_offer_name)
    TextView offer_name_tv;
    @BindView(R.id.iv_brand_image)
    ImageView brand_image_iv;
    @BindView(R.id.iv_first_offer_image)
    ImageView first_offer_iv;
    public ShopViewHolder(View view, ShopAdapter.ShopClickListener shopClickListener) {
        super(view);
        ButterKnife.bind(this, view);
        mView = view;
        mShopClickListener = shopClickListener;
        mView.setOnClickListener(this);
    }

    @Override
    protected void performBind(Shop shop) {
        shop_name_tv.setText(shop.getName());
        List<Offer> offerList = new ArrayList<>();
        offerList.addAll(shop.getOffers().values());
        offer_name_tv.setText(offerList.get(0).getDescription());
        Picasso.with(mView.getContext())
                .load(shop.getBrandImageURL())
                .fit()
                .into(brand_image_iv);
        Picasso.with(mView.getContext())
                .load(offerList.get(0).getOfferImageURL())
                .fit()
                .into(first_offer_iv);
        Log.d(TAG, "offer url = " + offerList.get(0).getOfferImageURL());
    }

    @Override
    public void onClick(View view) {
        mShopClickListener.onShopClick(getCurrentItem().getShopId());
        Log.d(TAG, "onClick item name = " + getCurrentItem().getName());
    }
}
