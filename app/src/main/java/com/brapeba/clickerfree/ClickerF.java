package com.brapeba.clickerfree;

/**
 * Created by joanmi on 14-Oct-15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClickerF extends Fragment
{
    int fragVal;
    CountO myCounter;
    double[] myPosition;
    View layoutView;

    static ClickerF init(int val)
    {
        ClickerF clickerFrag = new ClickerF();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        clickerFrag.setArguments(args);
        return clickerFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
        myPosition=new double[]{0,0};
        myCounter=new CountO(0,"Unnamed",myPosition);
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
        TextView tv = (TextView) layoutView.findViewById(R.id.clicker_ball);
        tv.setText(String.valueOf(myCounter.getClicks()));  // needed in case view is recreated (orientation change...)
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                long tempC=myCounter.getClicks() + 1;
                myCounter.setClicks(tempC);
                TextView tv2 = (TextView) getActivity().findViewById(R.id.clicker_ball);
                tv2.setText(String.valueOf(tempC));
            }
        });
    }

}