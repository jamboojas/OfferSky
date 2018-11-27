package com.example.abhiraj.offersky.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.viewholder.OfferViewHolder;

import java.util.List;

/**
 * Created by Abhiraj on 18-04-2017.
 */

public class OfferAdapter extends RecyclerView.Adapter<OfferViewHolder> {

    private static final String TAG = OfferAdapter.class.getSimpleName();
    private List<String> offer_image_urls;
    public OfferAdapter(List<String> offer_image_url){
        this.offer_image_urls = offer_image_url;
    }
    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_image_item, parent, false);
        if(view == null){
            Log.d(TAG, " view inflated is null");
        }
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
            holder.bindViews(offer_image_urls.get(position), position);
            Log.d(TAG, "offer at position " + position + " inflated");
    }

    @Override
    public int getItemCount() {
        return offer_image_urls.size();
    }
}
