package com.example.abhiraj.offersky.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.viewholder.ShopViewHolder;
import com.example.abhiraj.offersky.model.Shop;
import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Comparator;

/**
 * Created by Abhiraj on 14-04-2017.
 */

public class ShopAdapter extends SortedListAdapter<Shop>{

    private ShopClickListener mShopClickListener;

    public ShopAdapter(Context context, Class<Shop> itemClass, Comparator<Shop> comparator, ShopClickListener shopClickListener) {
        super(context, itemClass, comparator);
        mShopClickListener = shopClickListener;
    }


    @Override
    protected ViewHolder<? extends Shop> onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        final View view =inflater.inflate(R.layout.shop_card, parent, false);
        return new ShopViewHolder(view, mShopClickListener);
    }

    @Override
    protected boolean areItemsTheSame(Shop shop, Shop t1) {
        return shop.getShopId().equals(t1.getShopId());
    }

    @Override
    protected boolean areItemContentsTheSame(Shop shop, Shop t1) {
        return shop.equals(t1);
    }

    public interface ShopClickListener{

        void onShopClick(String shopId);
    }
}
