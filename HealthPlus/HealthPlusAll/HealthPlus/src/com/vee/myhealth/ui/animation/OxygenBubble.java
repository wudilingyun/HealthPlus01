package com.vee.myhealth.ui.animation;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * @author lingyun
 *
 */
public class OxygenBubble {
	private int currentDeep;
	private int yMiddle;
	private int yPosition;
	private int maxDeep;
	private int minDeep;
	private int increasement;
	private Bitmap bubbleImg;

	public OxygenBubble(OxygenThread ot, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4, int paramInt5) {
		bubbleImg = Bitmap.createScaledBitmap(ot.getBubble(), paramInt5,
				paramInt4, true);
		int i = ot.getY() + ot.getTubeHeight()*3/4;
		maxDeep = i;
		currentDeep = i;
		minDeep=ot.getY()+ ot.getTubeHeight()/4;
		yMiddle = ot.getX() + ot.getTubeWidth() / 2;
		yPosition = yMiddle;
		increasement = (paramInt3 * -1);
	}

	public final Bitmap getBubbleImg() {
		return bubbleImg;
	}

	public final void a(Canvas paramCanvas) {
		currentDeep += increasement;
		paramCanvas.drawBitmap(bubbleImg, yPosition, currentDeep, null);
		if (currentDeep <= minDeep) {
			currentDeep = maxDeep;
			yPosition = (new Random().nextInt(80) - 40) + yMiddle-15;
			Log.i("lingyun", "this.c=" + yPosition);
		}

	}
}
