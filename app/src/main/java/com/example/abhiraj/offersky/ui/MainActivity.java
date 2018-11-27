package com.example.abhiraj.offersky.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.abhiraj.offersky.BaseActivity;
import com.example.abhiraj.offersky.BuildConfig;
import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.R;
import com.example.abhiraj.offersky.SmoothActionBarDrawerToggle;
import com.example.abhiraj.offersky.WrapContentLinearLayoutManager;
import com.example.abhiraj.offersky.adapter.ShopAdapter;
import com.example.abhiraj.offersky.clickListener.ShopItemClickListenerImplementation;
import com.example.abhiraj.offersky.drawable.BadgeDrawable;
import com.example.abhiraj.offersky.geofencing.GeofenceService;
import com.example.abhiraj.offersky.model.Mall;
import com.example.abhiraj.offersky.model.Shop;
import com.example.abhiraj.offersky.utils.CouponUtils;
import com.example.abhiraj.offersky.utils.FirebaseUtils;
import com.example.abhiraj.offersky.utils.OfferSkyUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener, AHBottomNavigation.OnTabSelectedListener, SearchView.OnQueryTextListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TABSTATE_KEY = "tabstate";


    private enum TabState {Shopping, Food}
    private static LayerDrawable coupon_notification_icon;
    private static LayerDrawable event_notification_icon;

    private SmoothActionBarDrawerToggle mDrawerToggle;

    private TabState tabState = TabState.Shopping;
    private boolean isMallReady = false;

    private boolean isEarningSessionInProgress = false;
    private boolean isGeofenceBound = false;


    private SharedPreferences sharedPreferences;


    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.bottom_navigation) AHBottomNavigation bottomNavigation;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)TextView empty_tv;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        sharedPreferences = getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                Context.MODE_PRIVATE);
        String mallId = sharedPreferences.getString(Constants.SharedPreferences.MALL_ID, "MH_0253_CCM");

        if(getSupportActionBar() != null) {
            String title = sharedPreferences.getString(Constants.SharedPreferences.MALL_NAME, "OfferSky");
            Log.i(TAG, "mall name = " + title);
            setTitle(title);

        }

        ButterKnife.bind(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(dataReadyReceiver,
                new IntentFilter(Constants.Broadcast.MALL_DATA_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(dataReadyReceiver,
                new IntentFilter(Constants.Broadcast.VISITOR_DATA_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(couponAllottedReceiver,
                new IntentFilter(Constants.Broadcast.COUPON_ALLOT));

        if(savedInstanceState != null){
            tabState = (TabState) savedInstanceState.getSerializable(TABSTATE_KEY);
        }

        fab.setOnClickListener(this);


        //Replaced ActionBarDrawerToggle with its implementation SmoothActionBarDrawerToggle
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new SmoothActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // TODO: Replace with proper implementation
        // Get mall


        FirebaseUtils.getMall(this, mallId);
        // Show loading dialog if the connectivity is present
        showProgressDialog();

        setupTestRecyclerView();

        // UI functions
        createTabLayout();
        setupBottomNavigation();

    }

    @Override
    protected void onResume() {


        String earningStatus = sharedPreferences.getString(Constants.SharedPreferences.EARNING_STATUS,
                Constants.SharedPreferences.NOT_EARNING);
        if (earningStatus.equals(Constants.SharedPreferences.EARNING)){
            fab.setImageResource(R.drawable.ic_menu_manage);
        }
        else if(earningStatus.equals(Constants.SharedPreferences.NOT_EARNING)){
            fab.setImageResource(android.R.drawable.ic_dialog_email);
        }
        String progressStatus = sharedPreferences.getString(Constants.SharedPreferences.MALL_CHECK_STATUS,
                Constants.SharedPreferences.MALL_CHECK_STATUS_NOT_CHECKING);
        if(!progressStatus.equals(Constants.SharedPreferences.MALL_CHECK_STATUS_CHECKING)){
            OfferSkyUtils.hideProgressDialog();
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem couponMenuItem = menu.findItem(R.id.action_cart);
        MenuItem eventMenuItem = menu.findItem(R.id.action_events);

        coupon_notification_icon = (LayerDrawable) couponMenuItem.getIcon();
        event_notification_icon = (LayerDrawable) eventMenuItem.getIcon();
        // show the number of available coupons to the user as badge
        /*String couponCount = CouponUtils.getAllotableCouponCount(MainActivity.this,
                OfferSkyUtils.getCurrentMallId(MainActivity.this)) + "";
        Log.d(TAG, "coupon count obtained for badge = " + couponCount);
        setBadgeCount(this, coupon_notification_icon, couponCount);*/

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Log.d(TAG, "cart clicked");
            // setBadgeCount(this, coupon_notification_icon, "0");
            // Show the earned coupons
            Intent intent = new Intent(MainActivity.this, CouponActivity.class);
            startActivity(intent);
            return true;
        } 
        else if(id == R.id.action_events){
            Log.d(TAG, "events clicked");
            Intent intent = new Intent(MainActivity.this, EventActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_food) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();

        } else if(id == R.id.nav_coupons){

            mDrawerToggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, CouponActivity.class);
                    startActivity(intent);
                }
            });
            mDrawerLayout.closeDrawers();
        } else if (id == R.id.nav_events) {
            mDrawerToggle.runWhenIdle(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, EventActivity.class);
                    startActivity(intent);
                }
            });
            mDrawerLayout.closeDrawers();

        } else if (id == R.id.nav_change_mall) {
            if(!isEarningSessionInProgress) {

                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, MallSelectActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        //finish();
                    }
                });
                mDrawerLayout.closeDrawers();
            }

        } /*else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        // Get the name of the item clicked in drawer and pass it along to the new filter activity
        // TODO: add a constants interface for title and search terms
        else{
            Intent intent = new Intent(MainActivity.this, FilterActivity.class);
            intent.putExtra("Title", item.getTitle());
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TABSTATE_KEY, tabState);
    }

    //=============================================

    private void setupBottomNavigation(){
        Log.d(TAG, "setupBottomNavigation()");
        // Enable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);

        // Enable the translation of the FloatingActionButton
        bottomNavigation.manageFloatingActionButtonBehavior(fab);

        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(this);

        switch (tabState){

            case Shopping:
                // Create items
                AHBottomNavigationItem s_item1 = new AHBottomNavigationItem(R.string.shopping_tab_1, R.drawable.ic_menu_camera, R.color.tab1);
                AHBottomNavigationItem s_item2 = new AHBottomNavigationItem(R.string.shopping_tab_2, R.drawable.ic_menu_gallery, R.color.tab2);
                AHBottomNavigationItem s_item3 = new AHBottomNavigationItem(R.string.shopping_tab_3, R.drawable.ic_menu_send, R.color.tab3);
                AHBottomNavigationItem s_item4 = new AHBottomNavigationItem(R.string.shopping_tab_4, R.drawable.ic_menu_slideshow, R.color.tab4);

                // clear previous items (if any)
                bottomNavigation.removeAllItems();
                // Add items
                bottomNavigation.addItem(s_item1);
                bottomNavigation.addItem(s_item2);
                bottomNavigation.addItem(s_item3);
                bottomNavigation.addItem(s_item4);

                // set 1st Tab as open
                bottomNavigation.setCurrentItem(0);
                break;
            case Food:
                // Create items
                AHBottomNavigationItem f_item1 = new AHBottomNavigationItem(R.string.food_tab_1, R.drawable.ic_menu_camera, R.color.tab1);
                AHBottomNavigationItem f_item2 = new AHBottomNavigationItem(R.string.food_tab_2, R.drawable.ic_menu_gallery, R.color.tab2);
                AHBottomNavigationItem f_item3 = new AHBottomNavigationItem(R.string.food_tab_3, R.drawable.ic_menu_send, R.color.tab3);
                AHBottomNavigationItem f_item4 = new AHBottomNavigationItem(R.string.food_tab_4, R.drawable.ic_menu_slideshow, R.color.tab4);

                // clear previous items (if any)
                bottomNavigation.removeAllItems();
                // Add items
                bottomNavigation.addItem(f_item1);
                bottomNavigation.addItem(f_item2);
                bottomNavigation.addItem(f_item3);
                bottomNavigation.addItem(f_item4);

                // set 1st Tab as open
                bottomNavigation.setCurrentItem(0);

                break;

        }
    }

    // TODO: Properly Implement bottom nav tab selection and replace fragments with category filter logic
    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {

        Log.d(TAG, "btab " + position + " selected" + " and wasSelected = " + wasSelected+"");


        switch (position){

            case 0:
                switch (tabState){
                    case Shopping:
                        break;
                    case Food:
                        break;

                }
                // on app start mall may not be ready so when mall data is ready, onTAbselected is
                // called again by the broadcast and start filtering category is called
                if(isMallReady)
                    startFilteringCategory(position);
                // To add filters to the navigation drawer
                addNavigationItems(0);
                return true;

            case 1:
                switch (tabState){
                    case Shopping:
                        break;
                    case Food:
                        break;
                }
                if(isMallReady)
                    startFilteringCategory(position);
                addNavigationItems(1);
                return true;

            case 2:
                switch (tabState){
                    case Shopping:

                        break;
                    case Food:
                        break;

                }
                if(isMallReady)
                    startFilteringCategory(position);
                addNavigationItems(2);
                return true;

            case 3:
                switch (tabState){
                    case Shopping:

                        break;
                    case Food:
                        break;

                }
                if(isMallReady)
                    startFilteringCategory(position);
                addNavigationItems(3);
                return true;


        }
        return false;
    }



    private void createTabLayout(){
        Log.d(TAG, "createTabLayout()");
        if(tabLayout != null) {
            tabLayout.addTab(tabLayout.newTab().setText("Shopping"));
            tabLayout.addTab(tabLayout.newTab().setText("Food"));
            tabLayout.addOnTabSelectedListener(this);

            switch (tabState){
                case Shopping:
                    Log.d(TAG, "shopping tab select");
                    TabLayout.Tab s_tab = tabLayout.getTabAt(0);
                    try {
                        s_tab.select();
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                    break;
                case Food:
                    Log.d(TAG, "food tab select");
                    TabLayout.Tab f_tab = tabLayout.getTabAt(0);
                    try {
                        f_tab.select();
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                    break;
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d(TAG, "tab " + tab.getPosition() + " selected");
        switch (tab.getPosition()){
            case 0:
                tabState = TabState.Shopping;
                setupBottomNavigation();
                break;
            case 1:
                tabState = TabState.Food;
                setupBottomNavigation();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //Log.d(TAG, "tab " + tab.getPosition() + " unselected");
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //Log.d(TAG, "tab " + tab.getPosition() + " relected");
    }


    // TODO: Replace TestReyclerView with proper Implementation
    ShopAdapter shopAdapter;
    List<Shop> mModels;
    private void setupTestRecyclerView(){

        Log.d(TAG, "setupTestRecyclerView()");



        final Comparator<Shop> ALPHABETICAL_COMPARATOR = new Comparator<Shop>() {
            @Override
            public int compare(Shop a, Shop b) {
                return a.getName().compareTo(b.getName());
            }
        };
        if(shopAdapter == null) {
            shopAdapter = new ShopAdapter(this, Shop.class, ALPHABETICAL_COMPARATOR, new ShopItemClickListenerImplementation(this));
        }
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        recyclerView.setAdapter(shopAdapter);

        mModels = new ArrayList<>();

        Mall mall = OfferSkyUtils.getCurrentMall(this);

        if(mall!=null) {
            Collection<Shop> shopCollection = mall.getShops().values();
            mModels.addAll(shopCollection);
        }
        shopAdapter.edit().replaceAll(mModels)
                .commit();

        if(mModels.size() == 0 && !isNetworkAvailable(this)){
            fab.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            bottomNavigation.setVisibility(View.GONE);
            empty_tv.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            hideProgressDialog();
        }
        else{
            fab.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            bottomNavigation.setVisibility(View.VISIBLE);
            empty_tv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    // Shows or hides ui elements according to internet connectivity
    private void setUiState(){

        if(!isNetworkAvailable(this)){
            fab.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            bottomNavigation.setVisibility(View.GONE);
            empty_tv.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            //refresh the mall data
            sharedPreferences = getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                    Context.MODE_PRIVATE);
            String mallId = sharedPreferences.getString(Constants.SharedPreferences.MALL_ID, "MH_0253_CCM");

            FirebaseUtils.getMall(this, mallId);

        }
        else{
            fab.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            bottomNavigation.setVisibility(View.VISIBLE);
            empty_tv.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Shop> filteredModelList = filter(mModels, newText);
        shopAdapter.edit()
                .replaceAll(filteredModelList)
                .commit();
        recyclerView.scrollToPosition(0);
        return true;
    }

    private static List<Shop> filter(List<Shop> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Shop> filteredModelList = new ArrayList<>();
        for (Shop model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    // filter shops into tabs
    private void startFilteringCategory(int position) {

        List<String> tabFilterCategories = new ArrayList<>();
        switch (position){
            case 0:
                switch (tabState){
                    case Shopping:
                        /*tabFilterCategories.add("Formals");
                        tabFilterCategories.add("Ethnic");
                        tabFilterCategories.add("Party wear");
                        tabFilterCategories.add("Sports wear");*/

                        String fashionList[] = getResources().getStringArray(R.array.fashion);
                        tabFilterCategories.addAll(Arrays.asList(fashionList));
                        break;
                    case Food:
                        tabFilterCategories.add("Meals");
                        tabFilterCategories.add("Chaat");
                        tabFilterCategories.add("South Indian");
                        break;
                }
                break;

            case 1:
                switch (tabState){
                    case Shopping:
                        /*tabFilterCategories.add("Gifts & Toys");
                        tabFilterCategories.add("Electronics");
                        tabFilterCategories.add("Books & Music");
                        tabFilterCategories.add("Games");*/
                        String lifestyleList[] = getResources().getStringArray(R.array.lifestyle);
                        tabFilterCategories.addAll(Arrays.asList(lifestyleList));
                        break;
                    case Food:
                        tabFilterCategories.add("Cafe");
                        tabFilterCategories.add("Chinese");
                        break;
                }
                break;

            case 2:
                switch (tabState){
                    case Shopping:
                        /*tabFilterCategories.add("Accessories");
                        tabFilterCategories.add("Jewellery");
                        tabFilterCategories.add("Bags");
                        tabFilterCategories.add("Watches");*/

                        String accessoriesList[] = getResources().getStringArray(R.array.accessories);
                        tabFilterCategories.addAll(Arrays.asList(accessoriesList));
                        break;
                    case Food:
                        tabFilterCategories.add("Ice cream");
                        tabFilterCategories.add("Drinks");
                        tabFilterCategories.add("Dessert");
                        break;
                }
                break;

            case 3:
                switch (tabState){
                    case Shopping:
                        String fashionList[] = getResources().getStringArray(R.array.beauty_entertainment);
                        tabFilterCategories.addAll(Arrays.asList(fashionList));
                        break;
                    case Food:
                        tabFilterCategories.add("Starters");
                        break;
                }
                break;

        }
        final List<Shop> filteredModelList = categoryFilter(mModels, tabFilterCategories);
        shopAdapter.edit()
                .replaceAll(filteredModelList)
                .commit();
        recyclerView.scrollToPosition(0);
    }

    public static List<Shop> categoryFilter(List<Shop> models, List<String> categories)
    {
        final List<Shop> filteredModelList = new ArrayList<>();

        for(String query : categories) {

            for (Shop model : models) {

                if (model.getCategories().values().contains(query)) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    // Function for setting the badge for the toolbar icons
    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        Log.d(TAG, "setting badge count to " + count);
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);

    }

    private BroadcastReceiver dataReadyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received DATA READY broadcast ");
            if(intent.getAction().equals(Constants.Broadcast.MALL_DATA_READY))
            {
                hideProgressDialog();
                setupTestRecyclerView();
                isMallReady = true;
                onTabSelected(bottomNavigation.getCurrentItem(), true);

                // set the badge icon for coupon count
                String couponCount = CouponUtils.getAllotableCouponCount(MainActivity.this,
                        OfferSkyUtils.getCurrentMallId(MainActivity.this)) + "";
                Log.d(TAG, "coupon count obtained for badge = " + couponCount);
                setBadgeCount(MainActivity.this, coupon_notification_icon, couponCount);

                //Set badge icon for event count
                Mall mall = OfferSkyUtils.getCurrentMall(MainActivity.this);
                if(mall != null && mall.getEvents() != null) {
                    String eventCount = mall.getEvents().size() + "";
                    Log.d(TAG, "event count obtained for badge = " + eventCount);
                    setBadgeCount(MainActivity.this, event_notification_icon, eventCount);
                }
            }

        }
    };

    private BroadcastReceiver couponAllottedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // set the badge icon for coupon count
            String couponCount = CouponUtils.getAllotableCouponCount(MainActivity.this,
                    OfferSkyUtils.getCurrentMallId(MainActivity.this)) + "";
            Log.d(TAG, "coupon count obtained for badge = " + couponCount);
            setBadgeCount(MainActivity.this, coupon_notification_icon, couponCount);
        }
    };

    private BroadcastReceiver internet_state_change_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "internet state change broadcast received");
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            final android.net.NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isAvailable() || mobile.isAvailable()) {
                // Do something
                //setUiState();

                Log.d("Network Available ", "refreshing ui");
            }
        }
    };

    // To add items to the drawer according to the upper and the bottom nav tab position
    private void addNavigationItems(int position) {

        List<String> items = new ArrayList<>();

        switch (position){

            case 0:
                switch (tabState){
                    case Shopping:
                        String list[] = getResources().getStringArray(R.array.fashion);
                        items.addAll(Arrays.asList(list));

                        break;
                    case Food:
                        String flist[] = getResources().getStringArray(R.array.food);
                        items.addAll(Arrays.asList(flist));
                        break;
                }
                break;

            case 1:
                switch (tabState){
                    case Shopping:
                        String list[] = getResources().getStringArray(R.array.lifestyle);
                        items.addAll(Arrays.asList(list));
                        break;
                    case Food:
                        String flist[] = getResources().getStringArray(R.array.food);
                        items.addAll(Arrays.asList(flist));
                        break;
                }
                break;

            case 2:
                switch (tabState){
                    case Shopping:
                        String list[] = getResources().getStringArray(R.array.accessories);
                        items.addAll(Arrays.asList(list));
                        break;
                    case Food:
                        String flist[] = getResources().getStringArray(R.array.food);
                        items.addAll(Arrays.asList(flist));
                        break;
                }
                break;

            case 3:
                switch (tabState){
                    case Shopping:
                        String list[] = getResources().getStringArray(R.array.beauty_entertainment);
                        items.addAll(Arrays.asList(list));
                        break;
                    case Food:
                        String flist[] = getResources().getStringArray(R.array.food);
                        items.addAll(Arrays.asList(flist));
                        break;
                }
                break;
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null){
            final Menu menu = navigationView.getMenu();
            menu.clear();
            navigationView.inflateMenu(R.menu.base_drawer);
            for (String item : items) {
                menu.add(R.id.group1, Menu.NONE, 1, item);
            }
        }
    }


    //=====================================================

    @Override
    public void onClick(View view) {
        if (view == fab) {
            Log.d(TAG, "fab clicked");
            if (!isEarningSessionInProgress) {
                //Toast.makeText(this, "Clicked fab", Toast.LENGTH_SHORT).show();
                // check network availability
                if (!isNetworkAvailable(this)) {
                    Toast.makeText(this, getResources().getString(R.string.internet_not_available),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // check location status. if user is in the selected mall, start earning, else
                    // present to the user a list of malls to select from

                    //start location updates and report on the accuracy of location obtained
                    if (!checkPermission()) {
                        askPermission();
                    } else {
                        startEarningSequence();
                        OfferSkyUtils.showProgressDialog(this, getResources().getString(R.string.checking_mall));
                    }


                }
            }
        }
    }

    private void startEarningSequence() {
        // check for the gps state

        // if gps is on start the main service
        // check if gps is already on coz it will not trigger gpsCheckReceiver
        Log.d(TAG, "checking gps status and then starting geofence service if gps is already on");

        checkGpsSettings(locationRequest);      // also starts the geofence service when the user
        // turns on the gps or if the gps is already on

    }

    //====================================================================================


    // Defined in milli seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL =  1000;     // update every 1 second
    private final int FASTEST_INTERVAL = 500;     // if available then check after every 0.5 second
    private LocationRequest locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL)
            .setFastestInterval(FASTEST_INTERVAL);


    // Check for permission to access Location
    public boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    public void askPermission() {
        Log.d(TAG, "askPermission()");

        // Show rationale if the permission has been denied for the first time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION))
            {
                //Toast.makeText(this, "We need Gps to track you", Toast.LENGTH_SHORT)
                // .show();
            }
        }
        ActivityCompat.requestPermissions(
                this,
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                Constants.Permission.ACCESS_FINE_LOCATION_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case Constants.Permission.ACCESS_FINE_LOCATION_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    // Check Location Settings for gps state
                    //startLocationUpdates();
                    startEarningSequence();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    // Check if the GPS is turned on
    public void checkGpsSettings(LocationRequest locationRequest) {

        if(BuildConfig.DEBUG)  Log.d(TAG, "Checking GPS settings");

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
                .build();

        googleApiClient.connect();

        // Check whether the current location settings are satisfied
        PendingResult<LocationSettingsResult> result = LocationServices
                .SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result)
            {
                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();

                switch (status.getStatusCode())
                {
                    case LocationSettingsStatusCodes.SUCCESS:
                        if(BuildConfig.DEBUG)  Log.d(TAG, "GPS is already enabled");
                        startGeofenceService();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        if(BuildConfig.DEBUG)  Log.d(TAG, "Need to turn GPS on");
                        try
                        {
                            status.startResolutionForResult(
                                    MainActivity.this, Constants.Location.REQUEST_CHECK_LOCATION_SETTINGS);
                        } catch (IntentSender.SendIntentException e){}
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(MainActivity.this, "Cannot change GPS settings", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(BuildConfig.DEBUG)
            Log.d(TAG, "onActivityResult() called with: " + "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        switch (requestCode)
        {
            case Constants.Location.REQUEST_CHECK_LOCATION_SETTINGS:
                if(resultCode == Activity.RESULT_OK)
                {
                    Log.d(TAG, "gps enabled, sending broadcast");
                    startGeofenceService();
                }
                else{
                    //user has denied turning on the gps, show the user reason to use gps.
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage(R.string.GPS_required_message)
                            .setTitle(R.string.GPS_required_title);

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            checkGpsSettings(locationRequest);      // since gpsCheck has been called
                            // before => that the locationRequest object which is a class object is
                            // ready for use and hence we can supply the object to the gpsCheck.
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Log.d(TAG, "gps enable clarification denied");
                        }
                    });
                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;


        }

    }

    private void startGeofenceService(){
        Log.d(TAG, "starting geofence service");
        //Mall mall = Godlike.getMall(this, mallId);
        if(isMallReady) {
            Mall mall = FirebaseUtils.sMall;
            // TODO: Test this code robustly, see if the instance of sMall stays throughout app's life
            if (mall != null) {
                Intent intent = new Intent(this, GeofenceService.class);
                // TODO: check the validity of data, if incorrect issue a sorry message
                Log.d(TAG, "lat = " + mall.getLatitude());
                Log.d(TAG, "lon = " + mall.getLongitude());
                Log.d(TAG, "rad = " + mall.getRadius());
                intent.putExtra("latitude", mall.getLatitude());
                intent.putExtra("longitude", mall.getLongitude());
                intent.putExtra("radius", mall.getRadius());
                startService(intent);
            }
        }
        //bindGeofenceService();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(BuildConfig.DEBUG) Log.d(TAG, "connected to the location services");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "api Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "api Connection failed");
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mGeofenceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            if(BuildConfig.DEBUG) Log.d(TAG, "bound to the geofence listener service");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GeofenceService.GeofenceBinder binder = (GeofenceService.GeofenceBinder) service;
            //mGeofenceService = binder.getService();
            // not using geofenceservice anywhere hence commented

            isGeofenceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "disconnected to geofence service");
        }
    };

    // receives broadcast when the user is actually within the mall
    // TODO: make the receiver receive broadcasts even when the activity is paused
    // so that the actual state of earning is visible to the user even when the user switched the
    // app while the app is verifying the user's location
    private BroadcastReceiver fabIconReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "received fab icon update broadcast");
            OfferSkyUtils.hideProgressDialog();OfferSkyUtils.hideProgressDialog();
            if(intent.getAction().equals(Constants.Geofence.SHOW_EARNING_ICON)){
                fab.setImageResource(R.drawable.ic_menu_manage);
                isEarningSessionInProgress = true;
                // Request visitor number
                // update firebase database about the user entry
            }
            else if(intent.getAction().equals(Constants.Geofence.SHOW_DEFAULT_ICON)){
                fab.setImageResource(android.R.drawable.ic_dialog_email);
                isEarningSessionInProgress = false;

                new AlertDialog.Builder(MainActivity.this).setTitle(R.string.Invalid_mall_title)
                        .setMessage(R.string.select_diff_mall_message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MallSelectActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        }
    };

  /*  private BroadcastReceiver visitorNumberReadyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received Visitor DATA READY broadcast");
            // get the coupons according to the visitor number.
            //TODO: Get the 4 coupons and send them off to the step service along with milestones
            // start the pedometer service with the object put in the intent extra
            // coupon class has been made serializable
        }
    };*/

    @Override
    public void onStart(){
        Log.d(TAG, "on start");

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(fabIconReceiver, new IntentFilter(Constants.Geofence.SHOW_EARNING_ICON));
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(fabIconReceiver, new IntentFilter(Constants.Geofence.SHOW_DEFAULT_ICON));
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(internet_state_change_receiver, new IntentFilter(Constants.IntentFilter.INTERNET_CONNECTIVITY));

        if(isEarningSessionInProgress)
        {
            Intent geofenceIntent = new Intent(getApplicationContext(), GeofenceService.class);
            bindService(geofenceIntent, mGeofenceConnection, Context.BIND_AUTO_CREATE);
        }
        super.onStart();
    }

    @Override
    public void onStop(){

        super.onStop();
        Log.d(TAG, "on stop ");
        if(isGeofenceBound){
            Log.d(TAG, "unbinding geofence service");
            unbindService(mGeofenceConnection);
            isGeofenceBound = false;
        }
        try {
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .unregisterReceiver(fabIconReceiver);
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .unregisterReceiver(internet_state_change_receiver);
        }
        catch (Exception e){
            Log.e(TAG, e.toString());
        }

    }

}
