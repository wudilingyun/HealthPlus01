package com.vee.healthplus.ui.heahth_news;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class MyNewsPagerAdapter extends PagerAdapter {

	private List<ImageView> imageViews;
	private Context context;
	private List<Bitmap> bitmaplists;
	private List<String> urlList, tiList;
	private int position;
	private ImageLoader imageLoader;
	private TextView textView;
	private int i = 0;

	public MyNewsPagerAdapter(Context context, ImageLoader imageLoader,
			TextView tv_title) {
		super();
		this.context = context;
		imageViews = new ArrayList<ImageView>();
		this.bitmaplists = new ArrayList<Bitmap>();
		this.imageLoader = imageLoader;
		this.textView = tv_title;
	}

	public void addImages(List<String> urllist) {

		this.urlList = urllist;
		for (i = 0; i < 3; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setTag(urllist.get(i));
			imageView.setBackgroundResource(R.drawable.shop_img_defaultbg);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);

		}
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object instantiateItem(View arg0, final int arg1) {
		System.out.println("arg1=" + arg1);
		imageLoader.addTask(urlList.get(arg1), imageViews.get(arg1));

		((ViewPager) arg0).addView(imageViews.get(arg1));
		imageViews.get(arg1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						Health_news_detailsActivity.class);
				System.out.println("当前位置position" + position + "urllist:  "
						+ urlList.get(position));
				intent.putExtra("imgurl", urlList.get(arg1).toString());
				context.startActivity(intent);

			}
		});
		return imageViews.get(arg1);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {

	}

}
