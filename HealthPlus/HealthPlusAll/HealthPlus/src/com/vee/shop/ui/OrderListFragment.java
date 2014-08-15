package com.vee.shop.ui;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;

import com.vee.healthplus.util.user.ICallBack;
import com.vee.shop.bean.QueryOrderBean;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.vee.shop.util.TextUtil;
import com.yunfox.s4aservicetest.response.ServerOrderList;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class OrderListFragment extends BaseFragment implements ICallBack {
	private static final String TAG = "OrderListFragment";

	private ListView mListView;
	private myListAdapter mListAdapter;

	private ArrayList<QueryOrderBean> orderList;
	OnListActionListener mListActionListener;
	private final static int REQUEST_CODE_LOGIN = 100;
	private String backupJson;
	private int scrollPos;
	private int scrollTop;

	public interface OnListActionListener {
		public void OnListItemClick(String url, String id);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListActionListener = (OnListActionListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		orderList = new ArrayList<QueryOrderBean>();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(ApplicationUtils.getResId("layout",
				"shop_orderlist_list_fragment"), container, false);
		mListView = (ListView) localView.findViewById(ApplicationUtils
				.getResId("id", "orderlist_listfragment_listview"));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (null != orderList.get(position).getImgurl())
					mListActionListener.OnListItemClick(orderList.get(position)
							.getDetailurl(), orderList.get(position).getId());
			}
		});
		mListAdapter = new myListAdapter();
		return localView;
	}

	@Override
	public void onResume() {
		super.onResume();
		backupJson = settings.getString("OrderlistJsonString", "null");
		// if (orderList.size() <= 0) {
		// List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("username", MyApplication
		// .getUUID()));
		// params.add(new BasicNameValuePair("status",
		// Constants.ORDER_TYPE_ALL));
		// myHttpGetAsyncTask mHttpGetAsyncTask = new myHttpGetAsyncTask(
		// Constants.ORDERLIST_QUERY_URL, params, mContext);
		// mHttpGetAsyncTask.execute();

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		formData.add("status", Constants.ORDER_TYPE_ALL);
		new myGetOrderListTask(Constants.ACCOUNT_ORDERLIST_QUERY_URL, formData,
				mContext).execute();
		// } else {
		// mListView.setAdapter(mListAdapter);
		// mListView.setSelectionFromTop(scrollPos, scrollTop);
		// }
	}

	class myListAdapter extends BaseAdapter {

		Holder holder;

		class Holder {
			ImageView pic;
			TextView time;
			TextView price;
			TextView status;
		}

		@Override
		public int getCount() {
			return orderList.size();
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
			final QueryOrderBean qob = orderList.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(
						ApplicationUtils.getResId("layout",
								"shop_orderlist_list_item"), null);
				holder.time = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"order_list_item_time"));
				holder.price = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"order_list_price"));
				holder.status = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"order_list_status"));
				holder.pic = (ImageView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"order_list_item_photo"));
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			// Timestamp ts = new Timestamp(Long.parseLong(qob.getOrderdate()));
			// DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			// holder.time.setText(sdf.format(ts));
			holder.time.setText(qob.getOrderdate());
			/*
			 * Date date = new Date(); date = ts;
			 * holder.time.setText(date.toString());
			 */
			StringBuilder sb = new StringBuilder();
			char symbol = 165;
			sb.append(symbol);
			BigDecimal bPrice = new BigDecimal(qob.getTotalprice());
			sb.append(bPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			holder.price.setText(sb);
			String status = qob.getStatus();
			if (status.equals(Constants.ORDER_STATUS_NOPAY)) {
				holder.status.setText(ApplicationUtils.getResId("string",
						"order_list_status_nopay"));
			} else if (status.equals(Constants.ORDER_STATUS_WAIT_FOT_SEND)) {
				holder.status.setText(ApplicationUtils.getResId("string",
						"order_list_status_wait_for_send"));
			} else if (status.equals(Constants.ORDER_STATUS_WAIT_FOT_RECEIVE)) {
				holder.status.setText(ApplicationUtils.getResId("string",
						"order_list_status_wait_for_receive"));
			} else if (status.equals(Constants.ORDER_TYPE_DONE)) {
				holder.status.setText(ApplicationUtils.getResId("string",
						"order_list_type_done"));
			} else if (status.equals(Constants.ORDER_TYPE_UNDONE)) {
				holder.status.setText(ApplicationUtils.getResId("string",
						"order_list_type_undone"));
			} else if (status.equals(Constants.ORDER_TYPE_ALL)) {
				holder.status.setText(ApplicationUtils.getResId("string",
						"order_list_type_all"));
			}
			if ((null == qob.getImgurl()) || (qob.getImgurl().equals(""))) {
				holder.pic.setBackgroundResource(ApplicationUtils.getResId(
						"drawable", "shop_img_defaultbg"));
			} else {
				AQuery aq = new AQuery(holder.pic);
				File cacheDir = new File(Constants.EXTERNAL_STORAGE_PATH
						+ "/17VEEShop/photocache");
				AQUtility.setCacheDir(cacheDir);
				aq.image(qob.getImgurl(), Constants.isImgMemCache,
						Constants.isImgFileCache);
			}
			return convertView;
		}

	}

	class myGetOrderListTask extends ProtectTask {

		ServerOrderList serverOrderList;

		public myGetOrderListTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			getDialog().show();
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				serverOrderList = (SpringAndroidService
						.getInstance(getActivity().getApplication())
						.handleProtectOrder(actionUrl, formData,
								Constants.HTTP_TYPE_GET));
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				if (dialog.isShowing())
					dialog.dismiss();
				this.exception = e;
			}
			return super.doInBackground(params);
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			if (exception != null) {
				if (exception instanceof HttpClientErrorException) {
					HttpClientErrorException httpError = (HttpClientErrorException) exception;
					if (httpError.getStatusCode() == HttpStatus.UNAUTHORIZED) {
						// go login

					}
				} else if (exception instanceof MissingAuthorizationException) {
					// go login


				}
			}
			String json;
			if ((exception == null) && (serverOrderList != null)) {
				json = TextUtil.createOrderListJson(serverOrderList);
				editor.putString("OrderlistJsonString", json);
				editor.commit();
			} else {
				// json = backupJson;
				json = null;
			}
			orderList = httpUtil.parseOrderList(json);
			if (null != orderList) {
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
						if (orderList.size() > 0) {
							View v = mListView.getChildAt(0);
							scrollTop = (v == null) ? 0 : v.getTop();
						}
					};
				});
			}
		}

	}

	@Override
	public void onChange() {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		formData.add("status", Constants.ORDER_TYPE_ALL);
		new myGetOrderListTask(Constants.ACCOUNT_ORDERLIST_QUERY_URL, formData,
				mContext).execute();
	}

	@Override
	public void onCancel() {
		getActivity().finish();
	}
}
