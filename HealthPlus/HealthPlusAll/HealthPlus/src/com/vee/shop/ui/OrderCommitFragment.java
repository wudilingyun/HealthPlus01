package com.vee.shop.ui;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.vee.healthplus.common.MyApplication;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.bean.OrderBean;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.vee.shop.util.TextUtil;
import com.vee.shop.util.ToastUtil;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class OrderCommitFragment extends BaseFragment {
	private String fromCheckout;
	private ListView mListView;
	private myListAdapter mListAdapter;
	private TextView count;
	private TextView count_deliver;
	private Button btCommit;
	private String totalPrice;
	private ArrayList<CartItemBean> productList = new ArrayList<CartItemBean>();
	OnOrderListener mOnOrderListener;

	public interface OnOrderListener {
		public void OnOrderCommit(String result);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnOrderListener = (OnOrderListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		fromCheckout = bundle.getString("checkout_return_string");
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(ApplicationUtils.getResId("layout",
				"shop_ordercommit_fragment"), container, false);
		mListView = (ListView) localView.findViewById(ApplicationUtils
				.getResId("id", "shop_ordercommit_listview"));
		count = (TextView) localView.findViewById(ApplicationUtils.getResId(
				"id", "count"));
		count_deliver = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "count_deliver"));
		btCommit = (Button) localView.findViewById(ApplicationUtils.getResId(
				"id", "submit"));
		btCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
				formData.add("totalprice", totalPrice);
				new myCommitOrderTask(Constants.ACCOUNT_ORDER_COMMIT_URL,
						formData, mContext).execute();
			}
		});
		parseCheckout(fromCheckout);
		mListAdapter = new myListAdapter();
		mListView.setAdapter(mListAdapter);
		return localView;
	}

	public void parseCheckout(String result) {
		if (result == null || "".equals(result.trim())
				|| "null".equals(result.trim()))
			return;
		try {
			JSONObject jo_checkout = new JSONObject(result);
			if (null != jo_checkout.getString("totalprice")) {
				totalPrice = jo_checkout.getString("totalprice");
				BigDecimal bPrice = new BigDecimal(totalPrice);
				count.setText(mContext.getResources().getString(
						ApplicationUtils.getResId("string",
								"order_submit_count"))
						+ bPrice.setScale(2, BigDecimal.ROUND_HALF_UP)
								.toString());
			}
			if (null != jo_checkout.getString("deliveryprice")) {
				count_deliver.setText(mContext.getResources().getString(
						ApplicationUtils.getResId("string",
								"order_submit_count_deliver"))
						+ jo_checkout.getString("deliveryprice"));
			}
			if (jo_checkout.get("listproduct") instanceof JSONArray) {
				JSONArray ja_productlistArray = jo_checkout
						.getJSONArray("listproduct");
				for (int i = 0; i < ja_productlistArray.length(); i++) {
					CartItemBean cb = new CartItemBean();
					JSONObject jo_procut = ja_productlistArray.getJSONObject(i);
					if (null != jo_procut.getString("productid")) {
						cb.setId(jo_procut.getString("productid"));
					}
					if (null != jo_procut.getString("productimgurl")) {
						cb.setImgUrl(jo_procut.getString("productimgurl"));
					}
					if (null != jo_procut.getString("productitle")) {
						cb.setName(jo_procut.getString("productitle"));
					}
					if (null != jo_procut.getString("price")) {
						cb.setPrice(jo_procut.getString("price"));
					}
					if (null != jo_procut.getString("count")) {
						cb.setCount(jo_procut.getString("count"));
					}
					if (!jo_procut.getString("count").equals("0"))
						productList.add(cb);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class myListAdapter extends BaseAdapter {

		Holder holder;

		class Holder {
			ImageView pic;
			TextView title;
			TextView center;
			ImageView arrow;
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
			final CartItemBean cib = productList.get(position);
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
				holder.arrow = (ImageView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"arrow_right"));
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.arrow.setVisibility(View.GONE);
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
				aq.image(cib.getImgUrl(), Constants.isImgMemCache,
						Constants.isImgFileCache);
			}
			return convertView;
		}

	}

	class myCommitOrderTask extends ProtectTask {

		public myCommitOrderTask(String actionUrl,
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
				this.exception = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			if ((!TextUtils.isEmpty(result.toString()))
					&& (!result.toString().equals("null"))) {
				String json = TextUtil.createOrderJson(result);
				OrderBean oBean = httpUtil.parseOrder(json);
				if (oBean.getReturncode().equals("200")) {
					mOnOrderListener.OnOrderCommit(json);
					MyApplication.setCartNum(0);
					MyApplication.getCartMap().clear();
				} else if (oBean.getReturncode().equals("400")) {
					ToastUtil.show(mContext, oBean.getReturndesc());
				} else if (oBean.getReturncode().equals("401")) {
					ToastUtil.show(mContext, oBean.getReturndesc());
				}
			} else {
				ToastUtil
						.show(mContext, ApplicationUtils.getResId("string",
								"order_submit_exception_send_data"),
								Toast.LENGTH_SHORT);
			}
		}

	}
}
