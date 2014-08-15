package com.vee.shop.ui;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.vee.healthplus.common.MyApplication;

import com.vee.healthplus.util.user.ICallBack;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class ShoppingListFragment extends BaseFragment implements ICallBack {
	private static final String TAG = "ShoppingListFragment";

	private ListView mListView;
	private myListAdapter mListAdapter;
	private TextView shoppingTotal;
	private Button shoppingGoto;
	private final static int REQUEST_CODE_LOGIN = 100;
	private ArrayList<CartItemBean> cartItemList;
	OnListActionListener mListActionListener;
	OnButtonClickListener mButtonClickListener;

	public interface OnListActionListener {
		public void OnListItemClick(CartItemBean item);
	}

	public interface OnButtonClickListener {
		public void OnButtonClick();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListActionListener = (OnListActionListener) activity;
			mButtonClickListener = (OnButtonClickListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		cartItemList = new ArrayList<CartItemBean>();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(ApplicationUtils.getResId("layout",
				"shop_shopping_list_fragment"), container, false);
		shoppingTotal = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "shopping_listfragment_total"));
		shoppingGoto = (Button) localView.findViewById(ApplicationUtils
				.getResId("id", "shopping_listfragment_goto"));
		shoppingGoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mButtonClickListener.OnButtonClick();
			}
		});
		mListView = (ListView) localView.findViewById(ApplicationUtils
				.getResId("id", "shopping_listfragment_listview"));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (null != cartItemList.get(position))
					mListActionListener.OnListItemClick(cartItemList
							.get(position));
			}
		});
		mListAdapter = new myListAdapter();
		return localView;
	}

	@Override
	public void onResume() {
		super.onResume();
		// if (cartItemList.size() == 0) {
		// myHttpGetAsyncTask mHttpGetAsyncTask = new
		// myHttpGetAsyncTask(mContext);
		// mHttpGetAsyncTask.execute();

		// new myHttpGetProtectTask(Constants.ACCOUNT_QUERY_CART_URL, null,
		// mContext).execute();
		new myGetCartTask(Constants.ACCOUNT_QUERY_CART_URL, null, mContext)
				.execute();
		// } else {
		// mListAdapter.notifyDataSetChanged();
		// }
	}

	class myListAdapter extends BaseAdapter {

		Holder holder;

		class Holder {
			ImageView pic;
			TextView title;
			TextView center;
		}

		@Override
		public int getCount() {
			return cartItemList.size();
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
			final CartItemBean cib = cartItemList.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(
						ApplicationUtils.getResId("layout",
								"shop_shopping_list_item"), null);
				holder.title = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"shopping_cartlist_text_title"));
				holder.center = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"shopping_cartlist_text_center"));
				holder.pic = (ImageView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"shopping_cartlist_photo"));
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.title.setText(cib.getName());
			StringBuilder sb = new StringBuilder();
			sb.append(mContext.getResources()
					.getString(
							ApplicationUtils.getResId("string",
									"shop_cart_list_price")));
			char symbol = 165;
			sb.append(symbol);
			sb.append(cib.getPrice());
			sb.append(" ");
			sb.append(mContext.getResources().getString(
					ApplicationUtils.getResId("string", "shop_cart_list_num")));
			sb.append(cib.getCount());
			sb.append("\r\n");
			sb.append(mContext.getResources()
					.getString(
							ApplicationUtils.getResId("string",
									"shop_cart_list_total")));
			sb.append(symbol);
			BigDecimal priceBigDecimal = new BigDecimal(cib.getPrice());
			BigDecimal countBigDecimal = new BigDecimal(cib.getCount());
			sb.append(priceBigDecimal.multiply(countBigDecimal).toString());
			holder.center.setText(sb.toString());
			if ((null == cib.getImgUrl()) || (cib.getImgUrl().equals(""))) {
				holder.pic.setBackgroundResource(ApplicationUtils.getResId(
						"drawable", "shop_img_defaultbg"));
			} else {
				AQuery aq = new AQuery(holder.pic);
				File cacheDir = new File(Constants.EXTERNAL_STORAGE_PATH
						+ "/17VEEShop/photocache");
				AQUtility.setCacheDir(cacheDir);
				// aq.image(cib.getImgUrl(), MyApplication.isImgMemCache(),
				// MyApplication.isImgFileCache());
				aq.image(cib.getImgUrl(), Constants.isImgMemCache,
						Constants.isImgFileCache, 0, 0, null, AQuery.FADE_IN,
						AQuery.RATIO_PRESERVE);
			}
			return convertView;
		}

	}

	class myGetCartTask extends ProtectTask {

		public myGetCartTask(String actionUrl,
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
				return (SpringAndroidService.getInstance(getActivity()
						.getApplication()).handleProtect(actionUrl, formData,
						Constants.HTTP_TYPE_GET));
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
				return null;
			}
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
				MyApplication.setCartNum(0);
			} else if (null != result) {
				super.onPostExecute(result);
				if ((!TextUtils.isEmpty(result.toString()))
						&& (!result.toString().equals("null"))) {
					cartItemList = httpUtil.parseCartListNew(result);
					int count = 0;
					HashMap<String, CartItemBean> cartMap = httpUtil
							.parseCartMapNew(result);
					if (null != cartMap) {
						for (Map.Entry<String, CartItemBean> entry : cartMap
								.entrySet()) {
							count = count
									+ Integer.parseInt(entry.getValue()
											.getCount());
						}
					} else {
						count = 0;
					}
					MyApplication.setCartMap(cartMap);
					MyApplication.setCartNum(count);
				}
				BigDecimal totalBigDecimal = new BigDecimal("0");
				if (null != cartItemList) {
					for (int i = 0; i < cartItemList.size(); i++) {
						final CartItemBean itemBean = cartItemList.get(i);
						BigDecimal priceBigDecimal = new BigDecimal(
								itemBean.getPrice());
						BigDecimal countBigDecimal = new BigDecimal(
								itemBean.getCount());
						totalBigDecimal = totalBigDecimal.add(priceBigDecimal
								.multiply(countBigDecimal));
					}
					StringBuilder sb = new StringBuilder();
					sb.append(mContext.getResources().getString(
							ApplicationUtils.getResId("string",
									"shop_cart_total")));
					sb.append(" ");
					char symbol = 165;
					sb.append(symbol);
					sb.append(totalBigDecimal.toString());
					shoppingTotal.setText(sb.toString());
					mListView.setAdapter(mListAdapter);
				}

			}
		}

	}

	@Override
	public void onChange() {
		new myGetCartTask(Constants.ACCOUNT_QUERY_CART_URL, null, mContext)
				.execute();
	}

	@Override
	public void onCancel() {
		getActivity().finish();
	}
}
