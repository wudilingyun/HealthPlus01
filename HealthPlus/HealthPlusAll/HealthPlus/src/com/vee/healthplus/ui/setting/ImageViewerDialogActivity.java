package com.vee.healthplus.ui.setting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_utils.ImageFileCache;
import com.vee.healthplus.heahth_news_utils.ImageMemoryCache;

public class ImageViewerDialogActivity extends Activity {
	private ImageView iv, loadImageView;
	private RelativeLayout rl;
	private Animation news_loadAaAnimation;
	private FrameLayout loFrameLayout;
	private String bigurl;
	private Bitmap bmp = null;
	private ImageFileCache fileCache;
	private ImageMemoryCache memoryCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setWindowAnimations(R.style.dialog_updown);
		setContentView(R.layout.image_view_dialog_layout);
		WindowManager.LayoutParams lp=getWindow().getAttributes();
		lp.dimAmount=0.0f;
		getWindow().setAttributes(lp);
		rl = (RelativeLayout) findViewById(R.id.image_viewer_rl);
		iv = (ImageView) findViewById(R.id.image_viewer_big_iv);
		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loFrameLayout = (FrameLayout) findViewById(R.id.image_viewer_loading_fl);
		loadImageView = (ImageView) findViewById(R.id.image_viewer_loading_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);
		bigurl = getIntent().getStringExtra("bigurl");
		memoryCache = new ImageMemoryCache(this);
		fileCache = new ImageFileCache();
		new GetBigPhotoTask().execute();

	}

	private class GetBigPhotoTask extends AsyncTask<Void, Void, Integer> {
		int paramsX=0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadImageView.startAnimation(news_loadAaAnimation);

		}

		@Override
		protected Integer doInBackground(Void... params) {
			if (bigurl != null && !bigurl.equals("")) {
				bmp = memoryCache.getBitmapFromCache(bigurl);
				if (bmp == null)
					Log.i("lingyun", "get bigphoto from fileCache");
				bmp = fileCache.getImage(bigurl);
			}
			Log.i("lingyun", "bmp=" + bmp);
			if (bmp != null) {
				Log.i("lingyun", "bmp.width=" + bmp.getWidth() + " bmp.height="
						+ bmp.getHeight());
				paramsX=bmp.getWidth();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return bmp.getWidth();
			}

			return 0;

		}

		@Override
		protected void onPostExecute(Integer i) {
			loFrameLayout.setVisibility(View.INVISIBLE);
			loadImageView.clearAnimation();
			setParamsHandler.sendEmptyMessageDelayed(1, 100);
			showIvHandler.sendEmptyMessageDelayed(1, 300);

		}
		
		Handler setParamsHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				setParams(paramsX);
				iv.setImageBitmap(bmp);
			};
		};

		Handler showIvHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				iv.setVisibility(View.VISIBLE);
			};
		};
	}

	public void setParams(int i) {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		dm = this.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		LayoutParams params = (LayoutParams) iv.getLayoutParams();
		params.width = i;
		params.height = i;
		Log.i("lingyun", "screenWidth=" + screenWidth + "screenHeight="
				+ screenHeight);
		iv.setLayoutParams(params);
		
	}

}
