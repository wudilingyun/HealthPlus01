package com.vee.shop.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.shop.bean.StationBean;
import com.vee.shop.http.PublicGetTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;

public class StationListFragment extends BaseFragment {
	private static final String TAG = "StationListFragment";

	private ListView mListView;
	private myListAdapter mListAdapter;
	private ArrayList<StationBean> stationList;

	private String backupJson;
	private int scrollPos;
	private int scrollTop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stationList = new ArrayList<StationBean>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(ApplicationUtils.getResId("layout",
				"shop_servicestation_list_fragment"), container, false);
		mListView = (ListView) localView.findViewById(ApplicationUtils
				.getResId("id", "servicestation_listfragment_listview"));
		mListAdapter = new myListAdapter();
		return localView;
	}

	@Override
	public void onResume() {
		super.onResume();
		backupJson = settings.getString("StationlistJsonString", "null");
		if (stationList.size() == 0) {
			myHttpGetAsyncTask mHttpGetAsyncTask = new myHttpGetAsyncTask(
					Constants.ACCOUNT_QUERY_SERVICESTATION_URL, null, mContext);
			mHttpGetAsyncTask.execute();
		} else {
			mListView.setAdapter(mListAdapter);
			mListView.setSelectionFromTop(scrollPos, scrollTop);
		}
	}

	class myListAdapter extends BaseAdapter {

		Holder holder;

		class Holder {
			TextView name;
			TextView address;
			TextView tel;
			RelativeLayout telAll;
		}

		@Override
		public int getCount() {
			return stationList.size();
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
			final StationBean stb = stationList.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(
						ApplicationUtils.getResId("layout",
								"shop_servicestation_list_item"), null);
				holder.name = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"service_station_name_text"));
				holder.address = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"station_detail_address_item_text"));
				holder.tel = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"station_detail_tel_item_text"));
				holder.telAll = (RelativeLayout) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"service_station_tel_item"));
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.name.setText(stb.getName());
			holder.address.setText(getResources().getString(
					ApplicationUtils.getResId("string", "station_adress"))
					+ stb.getLocation() + stb.getAddress());
			holder.tel.setText(getResources().getString(
					ApplicationUtils.getResId("string", "station_tel"))
					+ stb.getPhone());
			holder.telAll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:" + stb.getPhone()));
					startActivity(intent);
				}
			});
			return convertView;
		}

	}

	class myHttpGetAsyncTask extends PublicGetTask {

		public myHttpGetAsyncTask(String actionUrl, List<NameValuePair> params,
				Context context) {
			super(actionUrl, params, context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			getDialog().show();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			getDialog().dismiss();
			String json;
			if ((!TextUtils.isEmpty(result)) && (!result.equals("null"))) {
				editor.putString("StationlistJsonString", result);
				editor.commit();
				json = result;
			} else {
				json = backupJson;
			}
			stationList = httpUtil.parseStationList(json);
			if (null != stationList) {
				mListView.setAdapter(mListAdapter);
				mListView.setOnScrollListener(new OnScrollListener() {

					@Override
					public void onScroll(AbsListView arg0, int arg1, int arg2,
							int arg3) {
					}

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
							// ListPos记录当前可见的List顶端的一行的位置
							scrollPos = mListView.getFirstVisiblePosition();
						}
						if (stationList.size() > 0) {
							View v = mListView.getChildAt(0);
							scrollTop = (v == null) ? 0 : v.getTop();
						}
					};
				});
			}
		}

	}
}
