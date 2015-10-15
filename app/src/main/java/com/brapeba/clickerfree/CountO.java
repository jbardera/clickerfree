package com.brapeba.clickerfree;

/**
 * Created by joanmi on 14-Oct-15.
 */
public class CountO
{
    private long clicks;        // how many clicks
    private String name;    // name to save the counting
    private double[] position;  // latitude & longitude

    public CountO(long iClicks, String iName, double[] iPosition)
    {
        this.clicks=iClicks;
        this.name=iName;
        this.position=iPosition;
    }

    public long getClicks()
    {
        return this.clicks;
    }

    public void setClicks(long iClicks)
    {
        this.clicks=iClicks;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String iName)
    {
        this.name=iName;
    }

    public double[] getPosition()
    {
        return this.position;
    }

    public void setPosition(double[] iPosition)
    {
        this.position=iPosition;
    }
}
