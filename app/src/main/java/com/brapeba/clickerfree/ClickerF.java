package com.brapeba.clickerfree;

/**
 * Created by joanmi on 14-Oct-15.
 */

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.SecureRandom;

public class ClickerF extends Fragment
{
    int fragVal;
    CountO myCounter;
    double[] myPosition;
    String myName;
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
    public void onSaveInstanceState (Bundle outState) // taking care when rotating screen
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
        if (savedInstanceState!=null) // recover after rotating screen
        {
            myCounter.setClicks(savedInstanceState.getLong("clicks"));
            myPosition[0]=savedInstanceState.getDouble("latitude");
            myPosition[1]=savedInstanceState.getDouble("longitude");
            myCounter.setPosition(myPosition);
            myCounter.setName(savedInstanceState.getString("myName"));
            //if (myCounter.getClicks()>0) tv.setText(String.valueOf(myCounter.getClicks())); // not needed, it is already at onResume()
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
        myPosition = new double[]{0, 0};
        myCounter = new CountO(0, myPosition);
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
        super.onCreateView(inflater, container, savedInstanceState);
        layoutView = inflater.inflate(R.layout.clicker_f, container,false);
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);
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
            @Override public void onClick(View view)
            {
                if (myCounter.getClicks() > 0)
                {
                    Snackbar.make(view, getString(R.string.saving), Snackbar.LENGTH_LONG).show();
                    //let's save the count: asking for a name
                    final AlertDialog fbuilder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_PopupOverlay)
                            .setPositiveButton(getString(R.string.save), null)
                            .setNegativeButton(getString(R.string.cancel), null)
                            .create();
                    final EditText edtName = new EditText(getActivity());
                    myName = new BigInteger(32, new SecureRandom()).toString(16);
                    edtName.setText(myName);
                    fbuilder.setView(edtName);
                    fbuilder.setTitle(getString(R.string.string1));
                    fbuilder.setOnShowListener(new DialogInterface.OnShowListener()
                    {
                        @Override
                        public void onShow(DialogInterface dialog)
                        {
                            final Button btnAccept = fbuilder.getButton(AlertDialog.BUTTON_POSITIVE);
                            btnAccept.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    if (edtName.getText().toString().isEmpty())
                                    {
                                        Toast.makeText(getActivity(), getString(R.string.string1), Toast.LENGTH_SHORT).show();
                                    } else
                                    {
                                        //let's save the count: now saving it!
                                        fbuilder.dismiss();
                                        myCounter.setName(edtName.getText().toString());
                                        if (!SaveC.saveToInternalStorage(myCounter, getActivity()))
                                        {
                                            // returned false -> handle error
                                        }
                                        SavedF.refreshTab(getActivity());
                                    }
                                }
                            });

                            final Button btnDecline = fbuilder.getButton(DialogInterface.BUTTON_NEGATIVE);
                            btnDecline.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    fbuilder.dismiss();
                                }
                            });
                        }
                    });
                    fbuilder.show();
                }
            }
         });
        FloatingActionButton fabReset = (FloatingActionButton) view.findViewById(R.id.fabreset);
        fabReset.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                myCounter.setClicks(0);
                TextView tv3 = (TextView) getActivity().findViewById(R.id.clicker_ball);
                tv3.setText(getString(R.string.clickme));
            }
        });
    }

    @Override public void onResume()
    {
        super.onResume();
        if (myCounter.getClicks()>0) tv.setText(String.valueOf(myCounter.getClicks()));
    }

    @Override public void onPause()
    {
        super.onPause();
    }

}