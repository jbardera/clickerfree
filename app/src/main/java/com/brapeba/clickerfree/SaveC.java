package com.brapeba.clickerfree;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joanmi on 16-Oct-15.
 */
public class SaveC
{
    static String FILENAME = "CountLists";
    static List<CountO> allCounts;
    static FileOutputStream fos;
    static FileInputStream fin;
    static final String TAG = "clickerfree";

    public SaveC()
    {
    }

    static public boolean saveToInternalStorage(CountO count,Activity activity)
    {
        // 1st reading all previous stored counts
        allCounts=readFromInternalStorage(activity);

        // now adding the one to save
        if (allCounts==null) { allCounts=new ArrayList<CountO>(); }
        allCounts.add(count);

        // and saving
        try
        {
            FileOutputStream fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(allCounts);
            of.flush();
            of.close();
            fos.close();
        } catch (Exception e) { Log.e(TAG, e.getMessage()); return false;}

        return true;
    }

    static public List<CountO> readFromInternalStorage(Activity activity)
    {
        List<CountO> toReturn=null;
        FileInputStream fis;

        try
        {
            fis = activity.openFileInput(FILENAME);
            ObjectInputStream oi = new ObjectInputStream(fis);
            toReturn = (List<CountO>) oi.readObject();
            oi.close();
        } catch (FileNotFoundException e)
        {
            Log.e(TAG, e.getMessage());
        } catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
       return toReturn;
    }

    static public boolean dumpMemToInternalStorage(List<CountO> aCounts,Activity activity)
    {
        // saving all mem to disk
        try
        {
            FileOutputStream fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(aCounts);
            of.flush();
            of.close();
            fos.close();
        } catch (Exception e) { Log.e(TAG, e.getMessage()); return false;}

        return true;
    }

}
