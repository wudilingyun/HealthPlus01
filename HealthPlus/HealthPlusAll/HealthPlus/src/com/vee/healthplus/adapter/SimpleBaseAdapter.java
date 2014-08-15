package com.vee.healthplus.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class SimpleBaseAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mLI;

    public SimpleBaseAdapter(Context context, List<T> list) {
        mList = list;
        mContext = context;
        mLI = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        if(mList==null) {
            return 0;
        } else {
            return mList.size();
        }
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

}
