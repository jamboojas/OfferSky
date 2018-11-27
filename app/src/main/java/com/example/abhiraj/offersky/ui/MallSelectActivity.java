package com.example.abhiraj.offersky.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.abhiraj.offersky.BaseActivity;
import com.example.abhiraj.offersky.Constants;
import com.example.abhiraj.offersky.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MallSelectActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner mStateSpinner;
    private Spinner mCitySpinner;
    private Spinner mMallSpinner;
    private Button selectBtn;
    private static final String TAG = MallSelectActivity.class.getSimpleName();
    private List<String> statesKey;
    private List<String> statesList;
    private List<String> citiesKey;
    private List<String> citiesList;
    private List<String> mallsKey;
    private List<String> mallsList;
    static ArrayAdapter<String> stateAdapter;
    static ArrayAdapter<String> cityAdapter;
    static ArrayAdapter<String> mallAdapter;

    private String mallId;
    private String mallName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_select);

        mStateSpinner = (Spinner) findViewById(R.id.state_spinner);
        mCitySpinner = (Spinner) findViewById(R.id.city_spinner);
        mMallSpinner = (Spinner) findViewById(R.id.mall_spinner);
        selectBtn = (Button) findViewById(R.id.select_mall_btn);
        selectBtn.setOnClickListener(this);
        prepareStateSpinner();

    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "OnDestroy of mall select");
        super.onDestroy();
    }

    private void prepareStateSpinner() {
        statesKey = new ArrayList<>();
        statesList = new ArrayList<String>();

        //statesList.add("Select State");
        stateAdapter = new ArrayAdapter<String>(MallSelectActivity.this, android.R.layout.simple_spinner_item, statesList);
        showProgressDialog();
        getData("states", statesKey, statesList, stateAdapter);


        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStateSpinner.setAdapter(stateAdapter);
        mStateSpinner.setOnItemSelectedListener(this);
    }




    private void prepareCitySpinner(String state_code) {
        citiesKey = new ArrayList<>();
        citiesList = new ArrayList<String>();

        cityAdapter = new ArrayAdapter<String>(MallSelectActivity.this, android.R.layout.simple_spinner_item, citiesList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        showProgressDialog();
        getData("state_cities/"+state_code, citiesKey, citiesList, cityAdapter);

        mCitySpinner.setAdapter(cityAdapter);
        mCitySpinner.setOnItemSelectedListener(this);
    }

    private void prepareMallSpinner(String city_code) {
        mallsKey = new ArrayList<>();
        mallsList = new ArrayList<String>();

        mallAdapter = new ArrayAdapter<String>(MallSelectActivity.this, android.R.layout.simple_spinner_item, mallsList);
        mallAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        showProgressDialog();
        getData("city_malls/" + city_code, mallsKey, mallsList, mallAdapter);
        mMallSpinner.setAdapter(mallAdapter);
        mMallSpinner.setOnItemSelectedListener(this);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        switch(adapterView.getId())
        {
            case  R.id.state_spinner:
                Log.d(TAG, adapterView.getItemAtPosition(pos).toString());
                Log.d(TAG, "key = " + statesKey.get(pos));
                prepareCitySpinner(statesKey.get(pos));
                break;
            case R.id.city_spinner:
                Log.d(TAG, adapterView.getItemAtPosition(pos).toString());
                Log.d(TAG, "key = " + citiesKey.get(pos));
                prepareMallSpinner(citiesKey.get(pos));
                break;
            case R.id.mall_spinner:
                Log.d(TAG, adapterView.getItemAtPosition(pos).toString());
                Log.d(TAG, "key = " + mallsKey.get(pos));
                // Store mallId in SharedPreferences
                mallId = mallsKey.get(pos);
                mallName = mallsList.get(pos);

                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getData(String ref, final List<String> key, final List<String> list, final ArrayAdapter<String> adapter)
    {

        //citiesList.add("Select City");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference(ref);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    key.add(ds.getKey().toString());
                    list.add(ds.getValue().toString());
                    Log.d(TAG, ds.getValue().toString());
                }
                //statesList.addAll(statesKey.values());
                hideProgressDialog();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {

        if(view == selectBtn)
        {
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SharedPreferences.USER_PREF_FILE,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.SharedPreferences.MALL_ID, mallId);
            editor.putString(Constants.SharedPreferences.MALL_NAME, mallName);
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}

