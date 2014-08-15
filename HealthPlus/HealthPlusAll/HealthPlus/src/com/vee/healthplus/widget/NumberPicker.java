package com.vee.healthplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.healthplus.R;

/**
 * Created by wangjiafeng on 13-11-19.
 */
public class NumberPicker extends LinearLayout implements View.OnClickListener {

    private EditText curValue_et;
    private TextView unit_tv;
    private int MINVALUE = 1;

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.pickview, null);
        Button plus_btn = (Button) view.findViewById(R.id.plus_btn);
        Button subtrack_btn = (Button) view.findViewById(R.id.subtrack_btn);
        curValue_et = (EditText) view.findViewById(R.id.curValue_et);
        unit_tv = (TextView) view.findViewById(R.id.unit_tv);
        plus_btn.setOnClickListener(this);
        subtrack_btn.setOnClickListener(this);
        setCurValue(MINVALUE);
        this.addView(view);
    }

    @Override
    public void onClick(View view) {
        int curValue = getCurValue();
        switch (view.getId()) {
            case R.id.plus_btn:
                curValue_et.setText(String.valueOf(curValue + 1));
                break;
            case R.id.subtrack_btn:
                if (curValue > MINVALUE) {
                    curValue_et.setText(String.valueOf(curValue - 1));
                }
                break;
        }
    }

    public int getCurValue() {
        if (curValue_et.getText().toString().length() <= 0) {
            return MINVALUE;
        }
        return Integer.valueOf(curValue_et.getText().toString());
    }

    public void setCurValue(int value) {
        curValue_et.setText(String.valueOf(value));
    }

    public void setMinValue(int value) {
        this.MINVALUE = value;
        curValue_et.setText(String.valueOf(value));
    }

    public void setUnitText(String text) {
        unit_tv.setText(text);
    }

}
