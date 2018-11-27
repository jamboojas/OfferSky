package com.example.abhiraj.offersky.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.model.Event;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhiraj on 20-04-2017.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = EventViewHolder.class.getSimpleName();

    private View mView;

    @BindView(R.id.iv_event)
    ImageView event_iv;
    @BindView(R.id.tv_event)
    TextView event_tv;
    public EventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mView = itemView;
    }

    public void bindViews(Event event){

        // TODO: Add event image url

        event_tv.setText(event.getDescription());
    }
}
