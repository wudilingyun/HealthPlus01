package com.vee.healthplus.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import com.vee.healthplus.R;

/**
 * Created by xujizhe on 13-12-17.
 */
public class ShadowTextView extends TextView {

    public ShadowTextView(Context context) {
        super(context);
    }

    public ShadowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setShadowLayer(0.1f, 0, 1, getResources().getColor(R.color.hp_w_header_view_white));
        getPaint().setShader(null);
        super.onDraw(canvas);

        getPaint().clearShadowLayer();
        getPaint().setShader(new LinearGradient(0, 0, 0, getHeight(), new int[]{getResources().getColor(R.color.sky_blue), getResources().getColor(R.color.dark_blue)}, new float[]{0.5f, 0.5f}, Shader.TileMode.CLAMP));
        super.onDraw(canvas);
    }


}
