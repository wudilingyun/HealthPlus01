package com.vee.shop.ui;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.yunfox.s4aservicetest.response.OrderPointBean;
import com.yunfox.s4aservicetest.response.PointBean;

public class PointOrderFragment extends BaseFragment {
	private static final String TAG = "PointOrderFragment";

	private OrderPointBean orderPointBean;
	private List<PointBean> scoreloglist;
	private ListView mListView;
	private myListAdapter mListAdapter;
	private TextView order_id;
	private TextView order_point;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		orderPointBean = (OrderPointBean) bundle
				.getSerializable("selected_pointorder");
		scoreloglist = orderPointBean.getScoreloglist();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater
				.inflate(ApplicationUtils.getResId("layout",
						"shop_pointorder_fragment"), container, false);
		order_id = (TextView) localView.findViewById(ApplicationUtils.getResId(
				"id", "order_id"));
		order_point = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "order_point"));
		StringBuilder sb = new StringBuilder();
		sb.append(getResources().getString(
				ApplicationUtils.getResId("string", "shop_point_list_title")));
		sb.append(orderPointBean.getOrderscore());
		order_point.setText(sb.toString());
		mListView = (ListView) localView.findViewById(ApplicationUtils
				.getResId("id", "point_order_fragment_listview"));
		mListAdapter = new myListAdapter();
		mListView.setAdapter(mListAdapter);
		return localView;
	}

	class myListAdapter extends BaseAdapter {

		Holder holder;

		class Holder {
			ImageView pic;
			TextView title;
			TextView detail;
		}

		@Override
		public int getCount() {
			return scoreloglist.size();
		}

		@Override
		public Object getItem(int item) {
			return item;
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final PointBean pb = scoreloglist.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(
						ApplicationUtils.getResId("layout",
								"shop_pointorder_list_item"), null);
				holder.title = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"product_title"));
				holder.detail = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"product_detail"));
				holder.pic = (ImageView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"product_photo"));
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.title.setText(pb.getProductname());
			StringBuilder sb = new StringBuilder();
			sb.append(getResources().getString(
					ApplicationUtils
							.getResId("string", "shop_point_list_title")));
			sb.append(pb.getPerproductscore());
			sb.append(" ");
			sb.append(getResources().getString(
					ApplicationUtils.getResId("string", "shop_cart_list_num")));
			sb.append(pb.getBuycount());
			sb.append("\r\n");
			sb.append(getResources()
					.getString(
							ApplicationUtils.getResId("string",
									"shop_cart_list_total")));
			BigDecimal scoreBigDecimal = new BigDecimal(pb.getPerproductscore());
			BigDecimal countBigDecimal = new BigDecimal(pb.getBuycount());
			sb.append(scoreBigDecimal.multiply(countBigDecimal).toString());
			holder.detail.setText(sb.toString());
			if ((null == pb.getImageurl()) || (pb.getImageurl().equals(""))) {
				holder.pic.setBackgroundResource(ApplicationUtils.getResId(
						"drawable", "shop_img_defaultbg"));
			} else {
				AQuery aq = new AQuery(holder.pic);
				File cacheDir = new File(Constants.EXTERNAL_STORAGE_PATH
						+ "/17VEEShop/photocache");
				AQUtility.setCacheDir(cacheDir);
				aq.image(pb.getImageurl(), Constants.isImgMemCache,
						Constants.isImgFileCache);
			}
			return convertView;
		}

	}

}
