package com.example.abhiraj.offersky.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.viewholder.EventViewHolder;
import com.example.abhiraj.offersky.model.Event;

import java.util.List;

/**
 * Created by Abhiraj on 20-04-2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private static final String TAG = EventAdapter.class.getSimpleName();

    List<Event> mEventList;

    public EventAdapter(List<Event> events){
        mEventList = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.bindViews(mEventList.get(position));
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
