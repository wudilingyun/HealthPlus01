package com.vee.shop.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.vee.healthplus.common.MyApplication;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.vee.shop.util.LogUtil;
import com.vee.shop.util.ToastUtil;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class ShoppingEditFragment extends BaseFragment {
	private static final String TAG = "ShoppingEditFragment";

	private CartItemBean cartItemBean;
	private ImageView pic;
	private TextView item_title;
	private TextView item_price;
	private Spinner item_count_spinnerSpinner;
	private Button item_del;
	private ArrayList<String> numArrayList = new ArrayList<String>();
	private ArrayAdapter<String> numAdapter;
	private int purchaseNum;
	private int modifyNum;
	OnDeleteListener mOnDeleteListener;

	public ShoppingEditFragment() {
		super();
	}

	public interface OnDeleteListener {
		public void OnDelte();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnDeleteListener = (OnDeleteListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		cartItemBean = bundle.getParcelable("cart_list_item_choosed");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(ApplicationUtils.getResId("layout",
				"shop_shopping_edit_fragment"), container, false);
		pic = (ImageView) localView.findViewById(ApplicationUtils.getResId(
				"id", "photo"));
		AQuery aq = new AQuery(pic);
		File cacheDir = new File(Constants.EXTERNAL_STORAGE_PATH
				+ "/17VEEShop/photocache");
		AQUtility.setCacheDir(cacheDir);
		aq.image(cartItemBean.getImgUrl(), Constants.isImgMemCache,
				Constants.isImgFileCache);
		item_title = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "title"));
		item_title.setText(cartItemBean.getName());
		item_price = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "price"));
		char symbol = 165;
		item_price.setText(symbol + cartItemBean.getPrice());
		item_count_spinnerSpinner = (Spinner) localView
				.findViewById(ApplicationUtils.getResId("id", "spinner"));
		int availableCount = Integer.parseInt(cartItemBean.getAvailablecount());
		purchaseNum = Integer.parseInt(cartItemBean.getCount());
		for (int i = 0; i < availableCount; i++) {
			numArrayList.add(String.valueOf(i + 1));
		}
		numAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, numArrayList);
		numAdapter.setDropDownViewResource(ApplicationUtils.getResId("layout",
				"shop_productdetail_spinner_textview"));
		item_count_spinnerSpinner.setPromptId(ApplicationUtils.getResId(
				"string", "goods_num"));
		item_count_spinnerSpinner.setAdapter(numAdapter);
		if (purchaseNum > availableCount) {
			item_count_spinnerSpinner.setSelection(availableCount - 1);
			purchaseNum = availableCount;
		}
		item_count_spinnerSpinner.setSelection(purchaseNum - 1);
		item_count_spinnerSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						modifyNum = Integer.parseInt(numArrayList.get(position));
						if (purchaseNum != modifyNum) {
							// String url = Constants.ADD_DEL_CART_URL;
							// url = url + cartItemBean.getId() + "/" + "mod/";
							// List<NameValuePair> params = new
							// ArrayList<NameValuePair>();
							// params.add(new BasicNameValuePair("username",
							// MyApplication.getUUID()));
							// params.add(new BasicNameValuePair("count", String
							// .valueOf(modifyNum)));
							// ModCartTask modCartTask = new ModCartTask(url,
							// params, mContext);
							// modCartTask.execute();

							String url = Constants.ACCOUNT_ADD_DEL_CART_URL
									+ cartItemBean.getId() + "/" + "mod/";
							MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
							formData.add("count", String.valueOf(modifyNum));
							new myModCartTask(url, formData, mContext)
									.execute();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						LogUtil.d(TAG, "onNothingSelected " + purchaseNum);
					}
				});
		item_del = (Button) localView.findViewById(ApplicationUtils.getResId(
				"id", "button_delete"));
		item_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// String url = Constants.ADD_DEL_CART_URL;
				// url = url + cartItemBean.getId() + "/" + "mod/";
				// List<NameValuePair> params = new ArrayList<NameValuePair>();
				// params.add(new BasicNameValuePair("username", MyApplication
				// .getUUID()));
				// params.add(new BasicNameValuePair("count",
				// String.valueOf(0)));
				// DelCartTask delCartTask = new DelCartTask(url, params,
				// mContext);
				// delCartTask.execute();

				String url = Constants.ACCOUNT_ADD_DEL_CART_URL
						+ cartItemBean.getId() + "/" + "mod/";
				MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
				formData.add("count", String.valueOf(0));
				new myDelCartTask(url, formData, mContext).execute();
			}
		});
		return localView;
	}

	class myModCartTask extends ProtectTask {

		public myModCartTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				return (SpringAndroidService.getInstance(getActivity()
						.getApplication()).handleProtect(actionUrl, formData,
						Constants.HTTP_TYPE_POST));
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			String returncode = httpUtil.parseServerResponse(result.toString());
			Log.d(TAG, result.toString() + "\n\r" + returncode);
			if ("200".equals(returncode)) {
				purchaseNum = modifyNum;
				item_count_spinnerSpinner.setSelection(purchaseNum - 1);
				String modId = cartItemBean.getId();
				HashMap<String, CartItemBean> cartMap = MyApplication
						.getCartMap();
				if ((cartMap != null) && (cartMap.containsKey(modId))) {
					cartMap.get(modId).setCount(String.valueOf(purchaseNum));
					int count = 0;
					for (Map.Entry<String, CartItemBean> entry : cartMap
							.entrySet()) {
						count = count
								+ Integer.parseInt(entry.getValue().getCount());
					}
					MyApplication.setCartMap(cartMap);
					MyApplication.setCartNum(count);
				}
				ToastUtil
						.show(mContext, ApplicationUtils.getResId("string",
								"shop_modcart_suc"), Toast.LENGTH_SHORT);
			} else {
				ToastUtil.show(mContext, ApplicationUtils.getResId("string",
						"shop_modcart_fail"), Toast.LENGTH_SHORT);

			}
		}

	}

	class myDelCartTask extends ProtectTask {

		public myDelCartTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				return (SpringAndroidService.getInstance(getActivity()
						.getApplication()).handleProtect(actionUrl, formData,
						Constants.HTTP_TYPE_POST));
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			Log.d(TAG,
					result.toString() + "\n\r"
							+ httpUtil.parseServerResponse(result.toString()));
			String returncode = httpUtil.parseServerResponse(result.toString());
			if ("200".equals(returncode)) {
				mOnDeleteListener.OnDelte();
				String delId = cartItemBean.getId();
				HashMap<String, CartItemBean> cartMap = MyApplication
						.getCartMap();
				if ((cartMap != null) && (cartMap.containsKey(delId))) {
					cartMap.remove(delId);
					int count = 0;
					for (Map.Entry<String, CartItemBean> entry : cartMap
							.entrySet()) {
						count = count
								+ Integer.parseInt(entry.getValue().getCount());
					}
					MyApplication.setCartMap(cartMap);
					MyApplication.setCartNum(count);
				}

			} else {
				ToastUtil.show(mContext, ApplicationUtils.getResId("string",
						"shop_modcart_fail"), Toast.LENGTH_SHORT);

			}
		}
	}

	/*
	 * class DelCartTask extends HttpPostAsyncTask {
	 * 
	 * public DelCartTask(String actionUrl, List<NameValuePair> params, Context
	 * context) { super(actionUrl, params, context); }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * super.onPostExecute(result); String returncode =
	 * httpUtil.parseServerResponse(result); if ("200".equals(returncode)) {
	 * mOnDeleteListener.OnDelte(); String delId = cartItemBean.getId();
	 * HashMap<String, CartItemBean> cartMap = MyApplication .getCartMap(); if
	 * (cartMap.containsKey(delId)) { cartMap.remove(delId); int count = 0; for
	 * (Map.Entry<String, CartItemBean> entry : cartMap .entrySet()) { count =
	 * count + Integer.parseInt(entry.getValue().getCount()); }
	 * MyApplication.setCartMap(cartMap); MyApplication.setCartNum(count); }
	 * 
	 * } else { ToastUtil.show(mContext, ApplicationUtils.getResId("string",
	 * "shop_modcart_fail"), Toast.LENGTH_SHORT);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 */
}
