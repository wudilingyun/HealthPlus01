package com.vee.healthplus.ui.heahth_news;

import com.vee.healthplus.R;
import com.vee.healthplus.http.StatisticsUtils;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.activity.MentalityActivity;
import com.vee.myhealth.activity.SleepActivity;
import com.vee.myhealth.activity.SubHealthActivity;
import com.vee.myhealth.activity.TiZhiActivity;
import com.vee.myhealth.activity.WeightLossActivity;
import com.vee.myhealth.ui.MyhealthFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Health_ValueBookListFragment extends Fragment {

	private GridView health_news_gridview;
	private MyGridViewAdapter gridViewAdapter;
	private int userId;

	public static Health_ValueBookListFragment newInstance() {
		return new Health_ValueBookListFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.health_news_fragment, container,
				false);
		userId=HP_User.getOnLineUserId(getActivity());
		init(view);
		data();
		return view;

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	

	void data() {
		gridViewAdapter = new MyGridViewAdapter(getActivity());
		health_news_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		health_news_gridview.setAdapter(gridViewAdapter);
		health_news_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView textView = (TextView) arg1
						.findViewById(R.id.news_item_name);
				String name = textView.getText().toString().trim();
				int id = (Integer) textView.getTag();
				Intent intent = new Intent(getActivity(),
						Health_ValuableBookActivity.class);
				intent.putExtra("id", id);
				Log.i("lingyun","Health_ValueBookListFragment.id.onclick="+id);
				switch (id) {
				case 1:
					StatisticsUtils.startFunction(StatisticsUtils.NEW_JSJF_ID);
					break;
				case 2:
					StatisticsUtils.startFunction(StatisticsUtils.NEW_JBYF_ID);
					break;
				case 3:
					StatisticsUtils.startFunction(StatisticsUtils.NEW_YST_ID);
					break;
				case 4:
					StatisticsUtils.startFunction(StatisticsUtils.NEW_QSYK_ID);
					break;
				case 5:
					StatisticsUtils.startFunction(StatisticsUtils.NEW_YEBK_ID);
					break;
				case 6:
					StatisticsUtils.startFunction(StatisticsUtils.NEW_JJJH_ID);
					break;
				case 7:
					StatisticsUtils.startFunction(StatisticsUtils.NEW_LXHT_ID);
					break;
				case 8:
					StatisticsUtils.startFunction(StatisticsUtils.NEW_ZXDB_ID);
					break;

				}
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});

	}

	void init(View view) {
		health_news_gridview = (GridView) view
				.findViewById(R.id.health_news_gridview);

	}

	public class MyGridViewAdapter extends BaseAdapter {

		protected LayoutInflater _mInflater;
		private Context ctx;

		private int icon[] = { R.drawable.news_jianfei, R.drawable.news_tizhi,
				R.drawable.news_life, R.drawable.news_happy,
				R.drawable.news_baby, R.drawable.news_rescue,
				R.drawable.news_sexs, R.drawable.news_fact };

		private int[] name = { R.string.yundong, R.string.jianya,
				R.string.yingyang, R.string.xiuxi, R.string.yongyao,
				R.string.shejiao, R.string.baojian, R.string.bingzheng, };
		private int[] nametg = { 1, 2, 3, 4, 5, 6, 7, 8 };
		/*
		 * private int[] egname = { R.string.egyundong, R.string.egjianya,
		 * R.string.egyingyang, R.string.egxiuxi, R.string.egyongyao,
		 * R.string.egshejiao, R.string.egbaojian, R.string.egbingzheng,
		 * R.string.egjijiu, R.string.egtizhi };
		 */
		private Class target[] = { SubHealthActivity.class,
				SleepActivity.class, MentalityActivity.class, null, null,
				WeightLossActivity.class };
		private int selectItem = 0;

		public MyGridViewAdapter(Context ctx) {
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
				view = _mInflater.inflate(R.layout.health_news_fragment_item,
						null);
				holder = new ViewHolder();
				holder.icon = (ImageView) view
						.findViewById(R.id.news_item_icon);
				holder.name = (TextView) view.findViewById(R.id.news_item_name);

				holder.egname = (TextView) view
						.findViewById(R.id.news_item_name_a);
				holder.lltestItem = (LinearLayout) view
						.findViewById(R.id.ll_test_item);
				// holder.rl = (RelativeLayout) view.findViewById(R.id.rl);
				view.setTag(holder);

			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.icon.setImageResource(icon[i]);
			holder.name.setText(ctx.getResources().getString(name[i]));
			holder.name.setTag(nametg[i]);
			// holder.egname.setText(egname[i]);
			/*
			 * if (i == selectItem) { holder.lltestItem
			 * .setBackgroundResource(R.drawable.test_item_selector_selected); }
			 * else { holder.lltestItem
			 * .setBackgroundResource(R.drawable.test_item_selector); }
			 */
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) ctx).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
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

		public String getTargetTitle(int pos) {
			return ctx.getResources().getString(name[pos]);
		}

		class ViewHolder {
			public ImageView icon;
			public TextView name;
			public TextView egname;
			public LinearLayout lltestItem;
			public RelativeLayout rl;
		}
	}

}
