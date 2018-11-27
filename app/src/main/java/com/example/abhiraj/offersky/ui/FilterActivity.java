package com.example.abhiraj.offersky.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.ShopAdapter;
import com.example.abhiraj.offersky.clickListener.ShopItemClickListenerImplementation;
import com.example.abhiraj.offersky.model.Shop;
import com.example.abhiraj.offersky.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity {

    private static final String TAG = FilterActivity.class.getSimpleName();

    private String searchTerm;
    ShopAdapter shopAdapter;
    List<Shop> mModels;

    @BindView(R.id.rv_filter)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)TextView empty_message_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);

        searchTerm = getIntent().getStringExtra("Title");
        // Ensure that the theme for the activity provides action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            String title = searchTerm;
            setTitle(title);

            setupRecyclerView();
        }
    }

    private void setupRecyclerView() {

        if(recyclerView != null){
            Log.d(TAG, "setupTestRecyclerView()");
            final Comparator<Shop> ALPHABETICAL_COMPARATOR = new Comparator<Shop>() {
                @Override
                public int compare(Shop a, Shop b) {
                    return a.getName().compareTo(b.getName());
                }
            };

            // click listener is implemented in another class.
            shopAdapter = new ShopAdapter(this, Shop.class, ALPHABETICAL_COMPARATOR, new ShopItemClickListenerImplementation(this));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(shopAdapter);

            mModels = new ArrayList<>();

            try {
                mModels.addAll(FirebaseUtils.sMall.getShops().values());
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }

            List<String> search = new ArrayList<>();
            search.add(searchTerm);
            mModels = MainActivity.categoryFilter(mModels, search);
            if(mModels.size() == 0)
            {
                recyclerView.setVisibility(View.GONE);
                empty_message_tv.setVisibility(View.VISIBLE);
            }

            shopAdapter.edit().replaceAll(mModels)
                    .commit();
        }
    }


}
