package com.brapeba.clickerfree;

/**
 * Created by joanmi on 14-Oct-15.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ClickerF extends Fragment
{
    int fragVal;
    CountO myCounter;
    double[] myPosition;             // latitude & longitude
    String myName;                   // name to save
    View layoutView;
    TextView tv;
    SharedPreferences mySettings;
    final String PREFS = "ClickerFreePrefs";
    final int BALLSIZE=60; // size in % of screen size (min of height,width) of the background circle to click

    static ClickerF init(int val)
    {
        ClickerF clickerFrag = new ClickerF();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        args.putInt("id", clickerFrag.getId());
        clickerFrag.setArguments(args);
        return clickerFrag;
    }

    @Override
    public void onSaveInstanceState (Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putLong("clicks", myCounter.getClicks());
        outState.putDouble("latitude", myCounter.getPosition()[0]);
        outState.putDouble("longitude", myCounter.getPosition()[1]);
        outState.putString("myName", myCounter.getName());
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null)
        {
            myCounter.setClicks(savedInstanceState.getLong("clicks"));
            myPosition[0]=savedInstanceState.getDouble("latitude");
            myPosition[1]=savedInstanceState.getDouble("longitude");
            myCounter.setPosition(myPosition);
            myCounter.setName(savedInstanceState.getString("myName"));
            tv.setText(String.valueOf(myCounter.getClicks()));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
        myPosition = new double[]{0, 0};
        myName = new BigInteger(32, new SecureRandom()).toString(16);
        myCounter = new CountO(0, myName, myPosition);
        mySettings=getActivity().getSharedPreferences(PREFS, 0);
        if (mySettings.getBoolean("swOff",false)) getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);;
        if (mySettings.getBoolean("swDim",false))
        {
            WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
            layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
            getActivity().getWindow().setAttributes(layoutParams);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        layoutView = inflater.inflate(R.layout.clicker_f, container,false);
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        tv = (TextView) layoutView.findViewById(R.id.clicker_ball);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
        // use  the minimum of both height and width to have a background circle
        int minSize=(int)((double)Math.min(displaymetrics.heightPixels,displaymetrics.widthPixels)*BALLSIZE)/100;
        params.height = minSize;
        params.width = minSize;
        tv.setLayoutParams(params);
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                long tempC = myCounter.getClicks() + 1;
                myCounter.setClicks(tempC);
                TextView tv2 = (TextView) view.findViewById(R.id.clicker_ball);
                tv2.setText(String.valueOf(tempC));
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}