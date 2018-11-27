package com.example.abhiraj.offersky.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.adapter.ShopTourAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopPhotoActivity extends AppCompatActivity {

    private static final String TAG = ShopPhotoActivity.class.getSimpleName();

    @BindView(R.id.shop_tour_rv)RecyclerView shop_tour_rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_photo);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        List<String> shopTourPhotos = intent.getExtras().getStringArrayList(Constants.IntentKeys.SHOP_PHOTOS_LIST);

        ShopTourAdapter shopTourAdapter = new ShopTourAdapter(this, shopTourPhotos);
        Log.d(TAG, "photo urls obtained = " + shopTourPhotos.toString());

        shop_tour_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        shop_tour_rv.setAdapter(shopTourAdapter);
    }
}
