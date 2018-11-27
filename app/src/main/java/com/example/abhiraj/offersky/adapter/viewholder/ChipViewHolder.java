package com.example.abhiraj.offersky.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.abhiraj.offersky.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhiraj on 17-04-2017.
 */

public class ChipViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_chip_name)
    TextView chip_name_tv;

    public ChipViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindChip(String category){
        chip_name_tv.setText(category);
    }
}
