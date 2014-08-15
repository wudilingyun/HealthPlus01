package com.vee.shop.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.vee.shop.activity.NewProductDetailActivity;
import com.vee.shop.bean.ProductBean;
import com.vee.shop.http.PublicGetTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;

public class AllProductFragment extends BaseFragment {

	private static final String TAG = "AllProductFragment";
	private GridView mGridView;
	private myGridAdatper mGridAdatper;
	private ArrayList<ProductBean> productList = new ArrayList<ProductBean>();
	private myHttpGetAsyncTask mHttpGetAsyncTask;

	private String backupJson;
	private int postionSel = 0;

	public AllProductFragment() {

	}

	public static AllProductFragment newInstance() {
		return new AllProductFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		productList = new ArrayList<ProductBean>();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater
				.inflate(ApplicationUtils.getResId("layout",
						"shop_allproduct_fragment"), container, false);
		mGridView = (GridView) localView.findViewById(ApplicationUtils
				.getResId("id", "shop_product_gridview"));
		return localView;
	}

	@Override
	public void onResume() {
		super.onResume();
		backupJson = settings.getString("AllProductJsonString", "null");
		if (null != productList) {
			if (productList.size() <= 0) {
				mHttpGetAsyncTask = new myHttpGetAsyncTask(
						Constants.ACCOUNT_REQUEST_ALL_PRODUCT, null, mContext);
				mHttpGetAsyncTask.execute();
			} else {
				mGridView.setAdapter(mGridAdatper);
				mGridView.setSelection(postionSel);
			}
		} else {
			mHttpGetAsyncTask = new myHttpGetAsyncTask(
					Constants.ACCOUNT_REQUEST_ALL_PRODUCT, null, mContext);
			mHttpGetAsyncTask.execute();
		}
	}

	class myGridAdatper extends BaseAdapter {

		Holder holder;

		class Holder {
			ImageView pic;
			TextView name;
			TextView price;
		}

		@Override
		public int getCount() {
			return productList.size();
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
			final int pos = position;
			String packagename = mContext.getPackageName();
			final ProductBean pb = productList.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(
						ApplicationUtils.getResId("layout",
								"shop_product_gridview_item", packagename),
						null);
				holder.name = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"shop_product_gridview_item_name", packagename));
				holder.pic = (ImageView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"shop_product_gridview_item_pic", packagename));
				holder.price = (TextView) convertView
						.findViewById(ApplicationUtils
								.getResId("id",
										"shop_product_gridview_item_price",
										packagename));
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.name.setText(pb.getName());
			String preString = getResources().getString(
					ApplicationUtils.getResId("string",
							"shop_product_marketprice"));
			char symbol = 165;
			holder.price.setText(preString + symbol + pb.getPrice());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					postionSel = pos;

					// Intent intent = new Intent(mContext,
					// ProductDetailActivity.class);
					Intent intent = new Intent(mContext,
							NewProductDetailActivity.class);
					intent.putExtra("product_url", pb.getUrl());
					intent.putExtra("product_id", pb.getId());
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);

				}
			});
			if ((null == pb.getImgUrl()) || (pb.getImgUrl().equals(""))) {
				holder.pic.setBackgroundResource(ApplicationUtils.getResId(
						"drawable", "shop_img_defaultbg", packagename));
			} else {
				AQuery aq = new AQuery(holder.pic);
				File cacheDir = new File(Constants.EXTERNAL_STORAGE_PATH
						+ "/17VEEShop/photocache");
				AQUtility.setCacheDir(cacheDir);
				aq.image(pb.getImgUrl(), Constants.isImgMemCache,
						Constants.isImgFileCache);
			}
			return convertView;
		}

	}

	// class myHttpGetAsyncTask extends HttpGetAsyncTask {
	//
	// public myHttpGetAsyncTask(String actionUrl, List<NameValuePair> params,
	// Context context) {
	// super(actionUrl, params, context);
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// getDialog().show();
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	// getDialog().dismiss();
	// String json;
	// if ((!TextUtils.isEmpty(result)) && (!result.equals("null"))) {
	// editor.putString("AllProductJsonString", result);
	// editor.commit();
	// json = result;
	// } else {
	// json = backupJson;
	// }
	// productList = httpUtil.parseProductList(json);
	// if (null != productList) {
	// mGridView.setAdapter(mGridAdatper);
	// }
	// }
	//
	// }

	class myHttpGetAsyncTask extends PublicGetTask {

		public myHttpGetAsyncTask(String actionUrl, List<NameValuePair> params,
				Context context) {
			super(actionUrl, params, context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// getDialog().show();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			getDialog().dismiss();
			String json;
			if ((!TextUtils.isEmpty(result)) && (!result.equals("null"))) {
				editor.putString("AllProductJsonString", result);
				editor.commit();
				json = result;
			} else {
				json = backupJson;
			}
			productList = httpUtil.parseProductList(json);
			if (null != productList) {
				if (null == mGridAdatper) {
					mGridAdatper = new myGridAdatper();
					mGridView.setAdapter(mGridAdatper);
				} else {
					mGridAdatper.notifyDataSetChanged();
				}
			}
		}

	}
}
