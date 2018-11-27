package com.example.abhiraj.offersky.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.viewholder.ChipViewHolder;

import java.util.List;

/**
 * Created by Abhiraj on 17-04-2017.
 */

public class ChipAdapter extends RecyclerView.Adapter<ChipViewHolder>{

    // TODO: Replace String with category model class
    private List<String> categories;
    // TODO: Replace String with category model class
    public ChipAdapter(List<String> categories)
    {
        this.categories = categories;
    }
    @Override
    public ChipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chip_item, parent, false);
        return new ChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChipViewHolder holder, int position) {
        holder.bindChip(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }



}
