package com.vee.healthplus.ui.setting;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;

public class ShareActivityAdapter extends BaseAdapter {

    private int[] iconResIdArr = {R.drawable.setting_share_sinaweibo,
            R.drawable.setting_share_tencentweibo,
            R.drawable.setting_share_wechat,
            R.drawable.setting_share_wechatmoments};
    private int[] nameResIdArr = {R.string.setting_share_xlwb,
            R.string.setting_share_txwb, R.string.setting_share_wx,
            R.string.setting_share_wxpy};
    private Context mContext;

    public ShareActivityAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return iconResIdArr.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = View.inflate(mContext, R.layout.setting_share_item, null);
            holder.icon = (ImageView) arg1.findViewById(R.id.icon);
            holder.name = (TextView) arg1.findViewById(R.id.name);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        holder.icon.setImageResource(iconResIdArr[arg0]);
        holder.name.setText(mContext.getResources().getString(
                nameResIdArr[arg0]));
        return arg1;
    }

    private final class ViewHolder {
        private ImageView icon;
        private TextView name;
    }

}
