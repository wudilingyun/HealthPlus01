package com.vee.healthplus.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.adapter.SimpleBaseAdapter;

public class PopupMenu extends Dialog implements OnItemClickListener {

    private Context mContext;

    private List<String> mMenuList = new ArrayList<String>();
    private List<Integer> mIconList = new ArrayList<Integer>();

    private OnPopupMenuClickListener mPopupMenuClickListener;

    private TextView mTitleView;

    private int mGroupId;
    
    private int itemLayoutRes;

    public PopupMenu(Context context) {
        super(context, R.style.Dialog);//popup_menu

        mContext = context;

        initPopupMenu();
    }

    private void initPopupMenu() {
        LinearLayout view = (LinearLayout)View.inflate(mContext, R.layout.custom_popupmenu, null);
        mTitleView = (TextView)view.findViewById(R.id.popupmenu_header_title);
   
        ListView contentView = (ListView)view.findViewById(R.id.popupmenu_content);
        contentView.setAdapter(new PopupMenuAdapter(mContext, mMenuList));
        contentView.setOnItemClickListener(this);

        setContentView(view);
       
    }
    
    public void setShowPosition(int y){
    	  WindowManager.LayoutParams params = getWindow()   
                  .getAttributes();     
          params.y = y;    
          getWindow().setAttributes(params);  
    }
    
    public static interface OnPopupMenuClickListener {
        public void OnPopupMenuClick(View v, int groupId, int itemId);
    }

    public void setOnPopupMenuClickListener(OnPopupMenuClickListener l) {
        mPopupMenuClickListener = l;
    }

    private class PopupMenuAdapter extends SimpleBaseAdapter<String> {

        public PopupMenuAdapter(Context context, List<String> list) {
            super(context, list);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout view = (RelativeLayout)mLI.inflate(itemLayoutRes, null); //R.layout.custom_popupmenu_item

           /* TextView title = (TextView)view.findViewById(R.id.popupmenu_item_title);
            title.setText(mList.get(position).toString());
            
            ImageView icon = (ImageView)view.findViewById(R.id.popupmenu_item_icon);
            if(icon!=null)
            	icon.setImageResource(mIconList.get(position));*/
            return view;
        }

    }
    
    public PopupMenu setItemLayout(int res){
    	this.itemLayoutRes=res;
    	return this;
    	
    }
    
    public PopupMenu setItemIcon(Integer[] icons){
    	this.mIconList=Arrays.asList(icons);
    	return this;
    	
    }
    public PopupMenu addItem(int groupId, CharSequence title) {
        mGroupId = groupId;
        mMenuList.add(title.toString());
        return this;
    }

    public PopupMenu addItem(int groupId, int titleRes) {
        mGroupId = groupId;
        mMenuList.add(mContext.getString(titleRes));
        return this;
    }
    
    public PopupMenu removeItem(int groupId,int position) {
        mGroupId = groupId;
        mMenuList.remove(position);
        return this;
    }

    public PopupMenu removeAllItem(int groupId) {
        mGroupId = groupId;
        mMenuList.clear();
        //mIconList.clear();
        return this;
    }

    public PopupMenu setHeaderTitle(CharSequence title) {
        mTitleView.setText(title);
        return this;
    }

    public PopupMenu setHeaderTitle(int titleRes) {
        mTitleView.setText(mContext.getString(titleRes));
        return this;
    }

    public void onItemClick(AdapterView<?> arg0, View view, int location, long arg3) {
        if (mPopupMenuClickListener != null) {
            mPopupMenuClickListener.OnPopupMenuClick(view, mGroupId, location);
        }

        //dismiss();
    } 
}
