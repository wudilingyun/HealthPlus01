package com.vee.shop.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.util.LinkedMultiValueMap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;

import com.vee.healthplus.util.user.HP_User;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.bean.ImageBean;
import com.vee.shop.bean.ProductDetailBean;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.vee.shop.util.ToastUtil;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class ProductDetailFragment extends BaseFragment {
	private static final String TAG = "ProductDetailFragment";
	private ProductDetailBean pdBean;
	private TextView tvName;
	private TextView tvPrice;
	private TextView tvScore;
	private LinearLayout product_buy_count_bar;
	private Button btUP;
	private TextView tvCount;
	private Button btDOWN;
	private Button btAddCart;
	private int currentCount;
	private int totalCount;

	OnCartAddedListener mCartAddedListener;

	public interface OnCartAddedListener {
		public void OnCartAdded(int num);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCartAddedListener = (OnCartAddedListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(R.layout.shop_productdetail_fragment,
				container, false);
		tvName = (TextView) localView
				.findViewById(R.id.shop_productdetail_name);
		if (null != pdBean.getName())
			tvName.setText(pdBean.getName());
		tvPrice = (TextView) localView
				.findViewById(R.id.shop_productdetail_price);
		if (null != pdBean.getPrice())
			tvPrice.setText(pdBean.getPrice());
		tvScore = (TextView) localView
				.findViewById(R.id.shop_productdetail_point);
		if (null != pdBean.getScore())
			tvScore.setText(pdBean.getScore());
		LinearLayout contentLayout = (LinearLayout) localView
				.findViewById(R.id.shop_productdetail_content);
		ArrayList<ImageBean> imageList = pdBean.getImageList();
		if (null != imageList) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			int pxMarginTop = ApplicationUtils.dip2px(mContext, 5);
			int pxMarginLeft = ApplicationUtils.dip2px(mContext, 2);
			layoutParams.setMargins(pxMarginLeft, pxMarginTop, pxMarginLeft, 0);
			boolean isFirstPic = true;
			for (int i = 0; i < imageList.size(); i++) {
				ImageBean imageBean = imageList.get(i);
				if (imageBean.getType().equals("text")) {
					TextView tv = new TextView(mContext);
					tv.setLayoutParams(layoutParams);
					tv.setPadding(ApplicationUtils.dip2px(mContext, 5), 0, 0, 0);
					tv.setText(imageList.get(i).getImageurl());
					contentLayout.addView(tv);
				} else if (imageBean.getType().equals("image")) {
					// LinearLayout parent = (LinearLayout) LayoutInflater.from(
					// mContext).inflate(
					// ApplicationUtils.getResId("layout",
					// "shop_productdetail_fragment_imageview"),
					// null, false);
					// ImageView iv = (ImageView) parent
					// .findViewById(ApplicationUtils.getResId("id",
					// "shop_productdetail_fragment_iv"));
					// parent.removeAllViews();
					ImageView iv = new ImageView(mContext);
					iv.setLayoutParams(layoutParams);
					iv.setScaleType(ImageView.ScaleType.CENTER);
					iv.setPadding(ApplicationUtils.dip2px(mContext, 5), 0,
							ApplicationUtils.dip2px(mContext, 5), 0);
					AQuery aq = new AQuery(iv);
					File cacheDir = new File(Constants.EXTERNAL_STORAGE_PATH
							+ "/17VEEShop/photocache");
					AQUtility.setCacheDir(cacheDir);
					aq.image(imageBean.getImageurl(), Constants.isImgMemCache,
							Constants.isImgFileCache);
					if (isFirstPic) {
						isFirstPic = false;
					} else {
						contentLayout.addView(iv);
					}
				}
			}
		}
		product_buy_count_bar = (LinearLayout) localView
				.findViewById(R.id.item_detail_relative_buy_count);
		tvCount = (TextView) localView
				.findViewById(R.id.shop_productdetail_text_buy_count);
		btUP = (Button) localView.findViewById(R.id.shop_productdetail_upBtn);
		btUP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentCount < totalCount) {
					tvCount.setText(String.valueOf(++currentCount));
				}
			}
		});
		btDOWN = (Button) localView
				.findViewById(R.id.shop_productdetail_downBtn);
		btDOWN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentCount > 1) {
					tvCount.setText(String.valueOf(--currentCount));
				}

			}
		});
		btAddCart = (Button) localView
				.findViewById(R.id.shop_productdetail_addcartBtn);
		btAddCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HashMap<String, CartItemBean> cartMap = MyApplication
						.getCartMap();
				int availableCount = Integer.parseInt(pdBean
						.getAvailablecount());
				if ((cartMap != null)
						&& cartMap.containsKey(pdBean.getId())
						&& ((currentCount + Integer.parseInt(MyApplication
								.getCartMap().get(pdBean.getId()).getCount())) > availableCount)) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "shop_addcart_toomany"),
							Toast.LENGTH_SHORT);
				} else {

					String url = Constants.ACCOUNT_ADD_DEL_CART_URL
							+ pdBean.getId() + "/" + "add";
					MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
					formData.add("count", String.valueOf(currentCount));
					new myAddCartTask(url, formData, mContext).execute();

				}
			}
		});
		if (null != pdBean.getAvailablecount()) {
			currentCount = Integer.parseInt(pdBean.getAvailablecount());
			totalCount = Integer.parseInt(pdBean.getAvailablecount());
			if (totalCount > 0) {
				tvCount.setText(pdBean.getAvailablecount());
			} else {
				currentCount = 0;
				product_buy_count_bar.setVisibility(View.INVISIBLE);
				btAddCart.setText(R.string.shop_addcart_no);
				btAddCart.setClickable(false);
			}
		}
		return localView;
	}

	public void setData(ProductDetailBean data) {
		this.pdBean = data;
	}

	class myAddCartTask extends ProtectTask {

		public myAddCartTask(String actionUrl,
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
			if (exception != null) {
				if (exception instanceof HttpClientErrorException) {
					HttpClientErrorException httpError = (HttpClientErrorException) exception;
					if (httpError.getStatusCode() == HttpStatus.UNAUTHORIZED) {
						// go login

					}
				} else if (exception instanceof MissingAuthorizationException) {
					// go login

				}
			} else if (exception == null) {
				if ((!TextUtils.isEmpty(result.toString()))
						&& (!result.toString().equals("null"))) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "shop_addcart_suc"), Toast.LENGTH_SHORT);
					CartItemBean cib = new CartItemBean();
					cib.setId(pdBean.getId());
					cib.setCount(String.valueOf(currentCount));
					HashMap<String, CartItemBean> cartMap = MyApplication
							.getCartMap();
					if (null != cartMap) {
						if (cartMap.containsKey(cib.getId())) {
							int count = Integer.parseInt(cartMap.get(
									cib.getId()).getCount());
							count = count + currentCount;
							cartMap.get(cib.getId()).setCount(
									String.valueOf(count));
						} else {
							if (null != cib) {
								// LogUtil.d(TAG, "cib.getid " + cib.getId());
							}
							try {
								cartMap.put(cib.getId(), cib);
							} catch (NullPointerException e) {
								e.printStackTrace();
								ToastUtil.show(mContext,
										ApplicationUtils.getResId("string",
												"shop_addcart_fail"),
										Toast.LENGTH_SHORT);
							}
						}
					}
					int total = MyApplication.getCartNum();
					total = total + currentCount;
					MyApplication.setCartNum(total);
					MyApplication.setCartMap(cartMap);
					mCartAddedListener.OnCartAdded(total);

				} else {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "shop_addcart_fail"), Toast.LENGTH_SHORT);
				}

				// btnAddCart.setText(ApplicationUtils.getResId("string",
				// "shop_addcart"));
				// btnAddCart.setClickable(true);

			}

		}
	}
}
