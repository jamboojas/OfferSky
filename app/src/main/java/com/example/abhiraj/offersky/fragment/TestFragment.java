package com.example.abhiraj.offersky.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abhiraj.offersky.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Abhiraj on 14-04-2017.
 */

public class TestFragment  extends Fragment{

    private static final String TAG = TestFragment.class.getSimpleName();

    private static final String MESSAGE = "param1";

    private String message;

    @BindView(R.id.tv)
    TextView mTextView;


    public TestFragment() {
    }

    public static TestFragment newInstance(String message){
        Log.d(TAG, "Fragment new Instance with text = " + message);
        TestFragment testFragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(MESSAGE, message);
        testFragment.setArguments(args);
        return testFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate ");
        if(getArguments() != null){
            message = getArguments().getString(MESSAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        mTextView.setText(message);
        Log.d(TAG, "onCreateView with text = " + message);
        return view;
    }


}
