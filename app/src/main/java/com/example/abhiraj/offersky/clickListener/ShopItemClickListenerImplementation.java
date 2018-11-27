package com.example.abhiraj.offersky.clickListener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.adapter.ShopAdapter;
import com.example.abhiraj.offersky.ui.ShopDetailsActivity;

/**
 * Created by Abhiraj on 18-04-2017.
 */

public class ShopItemClickListenerImplementation implements ShopAdapter.ShopClickListener {

    private static final String TAG = ShopItemClickListenerImplementation.class.getSimpleName();

    private Context mContext;

    public ShopItemClickListenerImplementation(Context context){
        mContext = context;
    }

    @Override
    public void onShopClick(String shopId) {
        Log.d(TAG, "shop with " + shopId + " clicked");
        Intent intent = new Intent(mContext, ShopDetailsActivity.class);
        intent.putExtra(Constants.IntentKeys.SHOP_ID, shopId);

        mContext.startActivity(intent);
    }
}
