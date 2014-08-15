package com.vee.myhealth.ui;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.myhealth.activity.MentalityActivity;
import com.vee.myhealth.activity.SleepActivity;
import com.vee.myhealth.activity.SubHealthActivity;
import com.vee.myhealth.activity.TestActivity;
import com.vee.myhealth.activity.TiZhiActivity;
import com.vee.myhealth.activity.WeightLossActivity;

/**
 * Created by xujizhe on 13-12-25.
 */
public class MyhealthMainAdapter extends BaseAdapter {

	protected LayoutInflater _mInflater;
	private static Context ctx;

	private int icon[] = { R.drawable.home_jiankang, R.drawable.home_tizhi,
			R.drawable.home_heart, R.drawable.home_gaoxuezhi,
			R.drawable.home_xuetang, R.drawable.home_jianfei };

	public static int[] name = { R.string.yajiankang, R.string.shuimianceshi,
			R.string.xinli, R.string.xuezhi, R.string.tangniaobing,
			R.string.jianfei };
	private Class target[] = { SubHealthActivity.class, SleepActivity.class,
			MentalityActivity.class, null, null, WeightLossActivity.class };
	private int selectItem = 0;

	public MyhealthMainAdapter(Context ctx) {
		this.ctx = ctx;
		_mInflater = LayoutInflater.from(this.ctx);
	}

	@Override
	public int getCount() {
		return name.length;
	}

	@Override
	public Object getItem(int i) {
		return ctx.getResources().getString(name[i]);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			view = _mInflater.inflate(R.layout.health_main_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) view.findViewById(R.id.item_icon);
			holder.name = (TextView) view.findViewById(R.id.item_name);
			holder.lltestItem = (LinearLayout) view
					.findViewById(R.id.ll_test_item);
			// holder.rl = (RelativeLayout) view.findViewById(R.id.rl);
			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.icon.setImageResource(icon[i]);
		holder.name.setText(ctx.getResources().getString(name[i]));
		/*
		 * if (i == selectItem) { holder.lltestItem
		 * .setBackgroundResource(R.drawable.test_item_selector_selected); }
		 * else { holder.lltestItem
		 * .setBackgroundResource(R.drawable.test_item_selector); }
		 */
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(dm);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				dm.widthPixels / 3, dm.widthPixels / 3);
		// holder.rl.setLayoutParams(lp);
		return view;
	}

	public Class getTargetClass(int pos) {
		return target[pos];
	}

	public void setSelectItem(int pos) {
		this.selectItem = pos;
		notifyDataSetChanged();
	}

	public static String getTargetTitle(int pos) {
		return ctx.getResources().getString(name[pos]);
	}

	class ViewHolder {
		public ImageView icon;
		public TextView name;
		public LinearLayout lltestItem;
		public RelativeLayout rl;
	}
}
