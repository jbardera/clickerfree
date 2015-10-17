package com.brapeba.clickerfree;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joanmi on 14-Oct-15.
 */
public class SavedF extends Fragment
{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static List<CountO> allCounts;
    private static List<CountO> afterDel= new ArrayList<CountO>();
    private static List<CountO> undoDel= new ArrayList<CountO>();
    private static ListView cListView;
    private static LinearLayout linL;
    private static SavedFCustomAdapter adapter;
    static final String TAG = "clickerfree";
    private View.OnClickListener mOnClickListener;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SavedF init(int sectionNumber)
    {
        SavedF fragment = new SavedF();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.saved_f, container, false);
        cListView = (ListView) rootView.findViewById(android.R.id.list);
        linL = (LinearLayout) rootView.findViewById(R.id.lltop);
        cListView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        cListView.setAdapter(adapter);
        cListView.requestLayout();

        mOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // undo
                for (CountO i : undoDel)
                {
                    i.setToBeDel(false);
                    allCounts.add(i);
                }
                SaveC.dumpMemToInternalStorage(allCounts, getActivity());
                undoDel.clear();
                adapter = new SavedFCustomAdapter(getActivity(),allCounts);
                cListView.setAdapter(adapter);
                cListView.requestLayout();
            }
        };
        FloatingActionButton fabd = (FloatingActionButton) view.findViewById(R.id.fabld);
        fabd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, getString(R.string.string2), Snackbar.LENGTH_LONG).setAction("Undo", mOnClickListener).show();
                List<CountO> delData = adapter.data;
                afterDel.clear();
                undoDel.clear();
                for (CountO i : delData)
                {
                    if (i.getToBeDel())
                    {
                        undoDel.add(i);
                    } else
                    {
                        afterDel.add(i);
                    }
                }
                allCounts.clear();
                SaveC.dumpMemToInternalStorage(afterDel, getActivity());
                refreshTab(getActivity()); // it reloads allCounts from storage
            }
        });
    }

    @Override public void onResume()
    {
        super.onResume();
        refreshTab(getActivity());
    }

    public static void refreshTab(Activity activity)
    {
        allCounts=SaveC.readFromInternalStorage(activity);
        if (allCounts==null) { allCounts=new ArrayList<CountO>(); }
        //adapter.notifyDataSetChanged(); // doesn't work! workaround as follows:
        adapter = new SavedFCustomAdapter(activity,allCounts);
        cListView.setAdapter(adapter);
        cListView.requestLayout();
    }

}
