package com.vee.healthplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.healthplus.R;

/**
 * Created by wangjiafeng on 13-11-20.
 */
public class TabButtons extends LinearLayout implements View.OnClickListener {

    private TextView sporttarget_tv, weightchange_tv, heartratechange_tv, sportdistance_tv;
    private int checkItem = -1;
    private View view;
    private TabButtonCheckChange listener;
    private int normalColorId = android.R.color.transparent;
    private int checkColorId = android.R.color.holo_red_dark;

    public TabButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = View.inflate(context, R.layout.tabbuttons, null);
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        this.addView(view);
        sporttarget_tv = (TextView) view.findViewById(R.id.sporttarget_tv);
        weightchange_tv = (TextView) view.findViewById(R.id.weightchange_tv);
        heartratechange_tv = (TextView) view.findViewById(R.id.heartratechange_tv);
        sportdistance_tv = (TextView) view.findViewById(R.id.sportdistance_tv);
        sporttarget_tv.setOnClickListener(this);
        weightchange_tv.setOnClickListener(this);
        heartratechange_tv.setOnClickListener(this);
        sportdistance_tv.setOnClickListener(this);
    }

    private void checkChange(int id) {
        sporttarget_tv.setBackgroundResource(R.drawable.userinfoleftnormal);
        weightchange_tv.setBackgroundResource(R.drawable.userinfocenternormal);
        heartratechange_tv.setBackgroundResource(R.drawable.userinfocenternormal);
        sportdistance_tv.setBackgroundResource(R.drawable.userinforightnormal);
        switch (id){
            case R.id.sporttarget_tv:
                sporttarget_tv.setBackgroundResource(R.drawable.userinfoleftbg);
                break;
            case R.id.weightchange_tv:
                weightchange_tv.setBackgroundResource(R.drawable.userinfocenterbg);
                break;
            case R.id.heartratechange_tv:
                heartratechange_tv.setBackgroundResource(R.drawable.userinfocenterbg);
                break;
            case R.id.sportdistance_tv:
                sportdistance_tv.setBackgroundResource(R.drawable.userinforightbg);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sporttarget_tv:
                checkItem = 0;
                break;
            case R.id.weightchange_tv:
                checkItem = 1;
                break;
            case R.id.heartratechange_tv:
                checkItem = 2;
                break;
            case R.id.sportdistance_tv:
                checkItem = 3;
                break;
        }
        checkChange(view.getId());
        if (listener != null) {
            listener.onCheckChange(checkItem);
        }
    }

    public void setOnTabButtonCheckChangeListener(TabButtonCheckChange listener) {
        this.listener = listener;
    }

    public interface TabButtonCheckChange {
        public void onCheckChange(int index);
    }
}
