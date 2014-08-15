package com.vee.myhealth.ui.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import com.vee.healthplus.R;

/**
 * @author lingyun
 *
 */
public class ProcessingLabel {
	private int a = 20;
	private int b;
	private int c=10;
	private int d;
	private int e;
	private int f;
	private int g = 0;
	private long h = 0L;
	private String[] i;
	private NinePatchDrawable j;
	private Drawable k;
	private Paint l;
	private boolean m = true;
	private boolean n;
	private boolean o;
	private Context p;

	public ProcessingLabel(Context paramContext,int fullWidth,int fullHeight) {
		this.p = paramContext;
		String[] arrayOfString = new String[4];
		arrayOfString[0] = "processing";
		arrayOfString[1] = "processing.";
		arrayOfString[2] = "processing..";
		arrayOfString[3] = "processing...";
		this.i = arrayOfString;
		this.j = ((NinePatchDrawable) paramContext.getResources().getDrawable(
				R.drawable.processing_blue_box));
		this.n = false;
		this.l = new Paint();
		this.l.setColor(Color.WHITE);
		this.l.setTextSize(45.0F);
		this.l.setAntiAlias(true);
		this.b=fullHeight-200;
	}

	public final void a(Canvas paramCanvas, long paramLong) {
		this.j.setBounds(this.c, this.b, this.c + 340, this.b + 120);
		this.j.draw(paramCanvas);

		paramCanvas.drawText(this.i[this.g], this.c + 40, this.b +60, this.l);

		// while (true) {
		if (this.n) {
			this.k.setAlpha(this.d);
			this.k.setBounds(this.c, this.b, this.c + 100, this.b + 50);
			this.k.draw(paramCanvas);
		}
		if (h != 0) {
			if (paramLong - h > 1000) {
				h = paramLong;
				this.g = (1 + this.g);
				int i2 = this.g;
				int i3 = 0;
				if (i2 < 4)
					i3 = this.g;
				this.g = i3;
			}
		} else {
			h = paramLong;
		}

		// }
	}
}
