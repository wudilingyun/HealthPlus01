package com.vee.healthplus.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;

public class CustomToast extends Toast {
    Context context;
    View layout;

    public CustomToast(Context context) {
        super(context);
        this.context = context;
    }

    private void createToast() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.custom_toast, null);
        this.setView(layout);
    }

    private void setText(String str) {
        TextView tv = (TextView) layout.findViewById(R.id.toasttext);
        tv.setText(str);
    }

    public void makeCustomText(Context context, String text, int duarion) {
        createToast();
        setText(text);
        setDuration(duarion);
        show();
    }

}

