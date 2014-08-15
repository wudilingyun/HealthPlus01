/**
 * 
 */
package com.vee.shop.ui;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;

import com.vee.healthplus.util.user.ICallBack;
import com.vee.shop.http.GetCartTask;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.yunfox.s4aservicetest.response.OrderPointBean;
import com.yunfox.s4aservicetest.response.TotalPointBean;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

/**
 * @author Felix
 * 
 */
public class PointListFragment extends BaseFragment implements ICallBack {
	private static final String TAG = "ShoppingListFragment";

	private ListView mListView;
	private myListAdapter mListAdapter;
	private TextView pointTotal;
	private TextView pointTotalTile;
	private List<OrderPointBean> orderPointList;
	OnPointListListener mOnPointListListener;

	public interface OnPointListListener {
		public void OnPointListItemClick(OrderPointBean pointOrder);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnPointListListener = (OnPointListListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater
				.inflate(ApplicationUtils.getResId("layout",
						"shop_point_list_fragment"), container, false);
		pointTotal = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "point_total"));
		pointTotalTile = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "point_total_title"));
		mListView = (ListView) localView.findViewById(ApplicationUtils
				.getResId("id", "pointlist_fragment_listview"));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mOnPointListListener.OnPointListItemClick(orderPointList
						.get(position));
			}
		});
		mListAdapter = new myListAdapter();
		return localView;
	}

	@Override
	public void onResume() {
		super.onResume();
		new myGetPointTask(Constants.ACCOUNT_POINT_QUERY_URL, null, mContext)
				.execute();
	}

	class myListAdapter extends BaseAdapter {

		Holder holder;

		class Holder {
			ImageView pic;
			TextView time;
			TextView point;
		}

		@Override
		public int getCount() {
			return orderPointList.size();
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
			final OrderPointBean opb = orderPointList.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(
						ApplicationUtils.getResId("layout",
								"shop_pointlist_list_item"), null);
				holder.time = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"point_list_item_time"));
				holder.point = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"point_list_status"));
				holder.pic = (ImageView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"point_list_item_photo"));
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			holder.time.setText(sdf.format(opb.getScoreloglist().get(0)
					.getCreatetime()));
			holder.point.setText(String.valueOf(opb.getOrderscore()));
			if ((null == opb.getScoreloglist().get(0).getImageurl())
					|| (opb.getScoreloglist().get(0).getImageurl().equals(""))) {
				holder.pic.setBackgroundResource(ApplicationUtils.getResId(
						"drawable", "shop_img_defaultbg"));
			} else {
				AQuery aq = new AQuery(holder.pic);
				File cacheDir = new File(Constants.EXTERNAL_STORAGE_PATH
						+ "/17VEEShop/photocache");
				AQUtility.setCacheDir(cacheDir);
				aq.image(opb.getScoreloglist().get(0).getImageurl(),
						Constants.isImgMemCache, Constants.isImgFileCache);
			}
			return convertView;
		}

	}

	class myGetPointTask extends ProtectTask {

		TotalPointBean totalPoint;

		public myGetPointTask(String actionUrl,
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
				totalPoint = (SpringAndroidService.getInstance(getActivity()
						.getApplication()).handleProtectPoint(actionUrl,
						formData, Constants.HTTP_TYPE_GET));
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
				totalPoint = null;
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
			if (null != totalPoint) {
				orderPointList = totalPoint.getClientorderscorelist();
				if (orderPointList.size() > 0) {
					mListView.setAdapter(mListAdapter);
					pointTotalTile.setVisibility(View.VISIBLE);
					pointTotal.setText(String.valueOf(totalPoint
							.getTotalscore()));
				} else {
					pointTotalTile.setVisibility(View.INVISIBLE);
				}
			}
		}

	}

	@Override
	public void onChange() {
		new myGetPointTask(Constants.ACCOUNT_POINT_QUERY_URL, null, mContext)
				.execute();
		new GetCartTask(null, null, mContext, getActivity()).execute();
	}

	@Override
	public void onCancel() {
		getActivity().finish();
	}

}
