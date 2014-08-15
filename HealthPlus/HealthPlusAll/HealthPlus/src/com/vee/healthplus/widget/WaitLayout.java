package com.vee.healthplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;

public class WaitLayout extends LinearLayout {

    private Context mContext;
    private RelativeLayout rl;
    private TextView tv;
    private ProgressBar progressBar;

    public WaitLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.mContext = context;
        init();
    }

    private void init() {
//        View view = ResourcesUtils.getView(mContext,
//                "managementmaster_waitlayout");
        LayoutInflater li = LayoutInflater.from(mContext);
        View view = li.inflate(
                R.layout.custom_waitlayout, null);
        this.addView(view);
//        rl = (RelativeLayout) view.findViewById(SystemMethod.getId(mContext,
//                "loading"));
//        tv = (TextView) view.findViewById(SystemMethod.getId(mContext,
//                "tv_loading"));
//        progressBar = (ProgressBar) view.findViewById(SystemMethod.getId(
//                mContext, "progressBar1"));
        show();
    }

    public void setBGColor(int color) {
        rl.setBackgroundColor(color);
    }

    public void hide() {
        rl.setVisibility(View.GONE);
    }

    public void show() {
        rl.setVisibility(View.VISIBLE);
    }

    public void noData(String str) {
        try {
            rl.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
//            String text = SystemMethod.getString(mContext, str);
//            tv.setText(text);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setText(String str) {
        try {
            rl.setVisibility(View.VISIBLE);
//            String text = SystemMethod.getString(mContext, str);
//            tv.setText(text);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
