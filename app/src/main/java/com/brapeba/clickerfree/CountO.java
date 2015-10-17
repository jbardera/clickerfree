package com.brapeba.clickerfree;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by joanmi on 14-Oct-15.
 */
public class CountO implements Serializable
{
    private long clicks;        // how many clicks
    private String name;    // name to save the counting
    private double[] position;  // latitude & longitude
    private Date firstClick,lastClick;  // begin time and end time of clicking
    private Boolean toBeDel=false;


    public CountO(long iClicks, double[] iPosition)
    {
        this.clicks=iClicks;
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

    public void setPosition(double[] iPosition)
    {
        this.position=iPosition;
    }

    public double[] getPosition()
    {
        return this.position;
    }

    public void setInitTime(Date iTime)
    {
        this.firstClick=iTime;
    }

    public Date getInitTime()
    {
        return this.firstClick;
    }

    public Date getEndTime()
    {
        return this.lastClick;
    }

    public void setEndTime(Date eTime)
    {
        this.lastClick=eTime;
    }

    public void setToBeDel(Boolean delete) {this.toBeDel=delete; }

    public Boolean getToBeDel() { return toBeDel; }

}
