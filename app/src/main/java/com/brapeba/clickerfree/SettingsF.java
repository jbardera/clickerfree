package com.brapeba.clickerfree;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by joanmi on 14-Oct-15.
 */
public class SettingsF extends Fragment
{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private Switch swOff,swDim;
    SharedPreferences mySettings;
    final String PREFS = "ClickerFreePrefs";
    SharedPreferences.Editor prefEditor;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsF init(int sectionNumber)
    {
        SettingsF fragment = new SettingsF();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsF()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.settings_f, container, false);
        swOff = (Switch) rootView.findViewById(R.id.switch1);
        swDim = (Switch) rootView.findViewById(R.id.switch2);
        mySettings=getActivity().getSharedPreferences(PREFS, 0);
        swOff.setChecked(mySettings.getBoolean("swOff", false));
        swDim.setChecked(mySettings.getBoolean("swDim", false));
        prefEditor=mySettings.edit();
        swOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                //save settings
                if (isChecked)
                {
                    prefEditor.putBoolean("swOff", true);
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else
                {
                    prefEditor.putBoolean("swOff", false);
                    getActivity().getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                prefEditor.commit();
            }
        });
        swDim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                    //save settings
                    if (isChecked)
                    {
                        prefEditor.putBoolean("swDim", true);
                        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
                        layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL;
                        getActivity().getWindow().setAttributes(layoutParams);
                    } else
                    {
                        prefEditor.putBoolean("swDim", false);
                        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
                        layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
                        getActivity().getWindow().setAttributes(layoutParams);
                    }
                    prefEditor.commit();
            }
        });
        return rootView;
    }
}
