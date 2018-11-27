package com.example.abhiraj.offersky.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.EventAdapter;
import com.example.abhiraj.offersky.model.Event;
import com.example.abhiraj.offersky.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {

    private static final String TAG = EventActivity.class.getSimpleName();

    private EventAdapter mEventAdapter;
    private List<Event> mModels;

    @BindView(R.id.rv_events)
    RecyclerView events_rv;
    @BindView(R.id.empty_view)
    TextView empty_msg_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ButterKnife.bind(this);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {
        Log.d(TAG, "insetupRecyclerView()");
        if(events_rv != null){
            Log.d(TAG, "setupRecyclerView()");

            mModels = new ArrayList<>();

            try {
                mModels.addAll(FirebaseUtils.sMall.getEvents().values());
                Log.d(TAG, "mall id when fetching events = " + FirebaseUtils.sMall.getMallId());
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }

            if(mModels.size() == 0)
            {
                events_rv.setVisibility(View.GONE);
                empty_msg_tv.setVisibility(View.VISIBLE);
            }

            else{
                Log.d(TAG, "no of events = " + mModels.size());
                mEventAdapter = new EventAdapter(mModels);
                events_rv.setLayoutManager(new LinearLayoutManager(this));
                events_rv.setAdapter(mEventAdapter);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
