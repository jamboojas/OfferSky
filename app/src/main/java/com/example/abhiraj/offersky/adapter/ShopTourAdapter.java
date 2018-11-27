package com.example.abhiraj.offersky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.viewholder.ShopTourViewHolder;

import java.util.List;

/**
 * Created by Abhiraj on 27-07-2017.
 */

public class ShopTourAdapter extends RecyclerView.Adapter<ShopTourViewHolder> {

    private static final String TAG = ShopTourAdapter.class.getSimpleName();

    private List<String> shop_tour_photos;
    private Context mContext;

    public ShopTourAdapter(Context context, List<String> shop_tour_photos){
        mContext = context;
        this.shop_tour_photos = shop_tour_photos;
    }

    @Override
    public ShopTourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_tour, parent, false);
        return new ShopTourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopTourViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder()");
        holder.bindView(shop_tour_photos.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount()");
        return shop_tour_photos.size();
    }
}
