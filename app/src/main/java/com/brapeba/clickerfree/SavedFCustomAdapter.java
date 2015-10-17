package com.brapeba.clickerfree;

/**
 * Created by joanmi on 16-Oct-15.
 */
/**
 * @author      Joanmi Bardera <joanmibb@gmail.com>
 * @version     1.0
 * @since       2015-10-16
 */

        import java.util.List;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.TextView;

public class SavedFCustomAdapter extends BaseAdapter
{
    public Context mContext;
    public List<CountO> data;
    private LayoutInflater mInflater = null;

    public SavedFCustomAdapter(Context context,List<CountO> counts)
    {
        super();
        mContext = context;
        data = counts;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder
    {
        TextView name;
        TextView clicks;
        CheckBox checked;
    }


    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if(convertView==null)
        {
            convertView = mInflater.inflate(R.layout.savedflist, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.lname);
            holder.clicks = (TextView) convertView.findViewById(R.id.lclicks);
            holder.checked = (CheckBox) convertView.findViewById(R.id.lbdel);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position).getName());
        holder.clicks.setText(String.valueOf(getItem(position).getClicks()));

        holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(position).setToBeDel(isChecked);
            }
        });

        return convertView;
    }

    public int getCount()
    {
        return data.size();
    }

    public CountO getItem(int position)
    {
        return data.get(position);
    }
    public long getItemId(int position)
    {
        return data.indexOf(getItem(position));
    }
}

