package com.vee.shop.activity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.bean.ProductDetailBean;
import com.vee.shop.http.GetCartTask;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.http.PublicGetTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.ui.ProductDetailFragment;
import com.vee.shop.ui.ProductParameterFragment;
import com.vee.shop.ui.ShopHeaderView;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.vee.shop.util.ToastUtil;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class NewProductDetailActivity extends BaseActivity implements
		OnClickListener, ICallBack {
	private static final String TAG = "NewProductDetailActivity";

	private ShopHeaderView hv;
	private ImageView ivUp;
	private ImageView leftBtn;
	private TextView cartCount;
	private ImageView rightBtn;
	private RadioGroup product_tab;
	private RadioButton product_tab_detail, product_tab_parameter;

	private String mProductUrl;
	private String mProductId;
	private String backupJson;
	private ProductDetailBean productDetailBean = null;

	private static final String FRAGMENT_TAG_DETAIL = "product_fragment_tag_detail";
	private static final String FRAGMENT_TAG_PARA = "product_fragment_tag_para";
	private FragmentManager localFragmentManager;
	private ProductDetailFragment mDetailFragment;
	private ProductParameterFragment mParaFragment;
	private boolean isAdd = true;

	private LinearLayout product_buy_count_bar;
	private Button btUP;
	private TextView tvCount;
	private Button btDOWN;
	private Button btAddCart;

	private int currentCount;
	private int totalCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_new_productdetail_activity);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		backupJson = settings.getString("ProductdetailJsonString" + mProductId,
				"null");
		if (handleIntent()) {
			new myHttpGetAsyncTask(mProductUrl, null, mContext).execute();
		}
		if (HP_User.getOnLineUserId(mContext) == 0) {
			cartCount.setVisibility(View.INVISIBLE);
		} else {
			int count = MyApplication.getCartNum();

			if (count > 0) {
				cartCount.setVisibility(View.VISIBLE);
				cartCount.setText(String.valueOf(count));
			} else {
				cartCount.setVisibility(View.INVISIBLE);
			}
		}

	}

	private void initView() {
		ivUp = (ImageView) findViewById(R.id.shop_productdetail_up_pic);
		hv = (ShopHeaderView) findViewById(R.id.header);
		hv.setHeaderTitle(getResources().getString(R.string.shop_txt));
		leftBtn = (ImageView) findViewById(R.id.header_lbtn_img);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);
		rightBtn = (ImageView) findViewById(R.id.header_rbtn_img);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(this);
		cartCount = (TextView) findViewById(R.id.shop_title_bar_shopping_count);
		product_tab = (RadioGroup) findViewById(R.id.productdetail_tab);
		product_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.productdetail_tab_detail:
					chooseDetail(productDetailBean);
					break;
				case R.id.productdetail_tab_parameter:
					choosePara(productDetailBean);
					break;
				default:
					break;
				}
			}
		});
		product_tab_detail = (RadioButton) findViewById(R.id.productdetail_tab_detail);
		product_tab_detail.setOnClickListener(this);
		product_tab_parameter = (RadioButton) findViewById(R.id.productdetail_tab_parameter);
		product_tab_parameter.setOnClickListener(this);
		localFragmentManager = getSupportFragmentManager();
		mDetailFragment = (ProductDetailFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_DETAIL);
		mParaFragment = (ProductParameterFragment) localFragmentManager
				.findFragmentByTag(FRAGMENT_TAG_PARA);

		product_buy_count_bar = (LinearLayout) findViewById(R.id.item_detail_relative_buy_count);
		tvCount = (TextView) findViewById(R.id.shop_productdetail_text_buy_count);
		btUP = (Button) findViewById(R.id.shop_productdetail_upBtn);
		btUP.setOnClickListener(this);
		btDOWN = (Button) findViewById(R.id.shop_productdetail_downBtn);
		btDOWN.setOnClickListener(this);
		btAddCart = (Button) findViewById(R.id.shop_productdetail_addcartBtn);
		btAddCart.setOnClickListener(this);
	}

	private boolean handleIntent() {
		Intent localIntent = getIntent();
		this.mProductUrl = localIntent.getStringExtra("product_url");
		this.mProductId = localIntent.getStringExtra("product_id");
		return (!TextUtils.isEmpty(mProductUrl));
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
				editor.putString("ProductdetailJsonString" + mProductId, result);
				editor.commit();
				json = result;
			} else {
				json = backupJson;
			}
			productDetailBean = httpUtil.parseProductDetail(json);
			if (null != productDetailBean) {
				chooseDetail(productDetailBean);
				refreshUI();
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_lbtn_img:
			finish();
			break;
		case R.id.header_rbtn_img:
			Intent intent = new Intent(mContext, ShoppingActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.shop_productdetail_upBtn:
			if (currentCount < totalCount) {
				tvCount.setText(String.valueOf(++currentCount));
			}
			break;
		case R.id.shop_productdetail_downBtn:
			if (currentCount > 1) {
				tvCount.setText(String.valueOf(--currentCount));
			}
			break;
		case R.id.shop_productdetail_addcartBtn:
			if (HP_User.getOnLineUserId(mContext) == 0) {
				
			} else {
				HashMap<String, CartItemBean> cartMap = MyApplication
						.getCartMap();
				if (null != productDetailBean) {
					int availableCount = Integer.parseInt(productDetailBean
							.getAvailablecount());
					if ((cartMap != null)
							&& cartMap.containsKey(productDetailBean.getId())
							&& ((currentCount + Integer.parseInt(MyApplication
									.getCartMap()
									.get(productDetailBean.getId()).getCount())) > availableCount)) {
						ToastUtil.show(mContext, ApplicationUtils.getResId(
								"string", "shop_addcart_toomany"),
								Toast.LENGTH_SHORT);
					} else {

						String url = Constants.ACCOUNT_ADD_DEL_CART_URL
								+ productDetailBean.getId() + "/" + "add";
						MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
						formData.add("count", String.valueOf(currentCount));
						new myAddCartTask(url, formData, mContext).execute();

					}
				} else {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "shop_addcart_fail"), Toast.LENGTH_SHORT);
				}

			}

			break;
		default:
			break;
		}
	}

	public void refreshUI() {
		if ((productDetailBean.getImageList().size() > 0)
				&& (null != productDetailBean.getImageList().get(0)
						.getImageurl())) {
			AQuery aq = new AQuery(ivUp);
			File cacheDir = new File(Constants.EXTERNAL_STORAGE_PATH
					+ "/17VEEShop/photocache");
			AQUtility.setCacheDir(cacheDir);
			aq.image(productDetailBean.getImageList().get(0).getImageurl(),
					Constants.isImgMemCache, Constants.isImgFileCache);
		}
		if (null != productDetailBean.getAvailablecount()) {
			currentCount = Integer.parseInt(productDetailBean
					.getAvailablecount());
			totalCount = Integer
					.parseInt(productDetailBean.getAvailablecount());
			if (totalCount > 0) {
				tvCount.setText(productDetailBean.getAvailablecount());
				btAddCart.setClickable(true);
			} else {
				currentCount = 0;
				product_buy_count_bar.setVisibility(View.INVISIBLE);
				btAddCart.setText(R.string.shop_addcart_no);
				btAddCart.setClickable(false);
			}
		}
	}

	public void chooseDetail(ProductDetailBean productDetailBean) {

		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		if (mDetailFragment == null) {
			mDetailFragment = new ProductDetailFragment();
		}
		if (productDetailBean != null)
			mDetailFragment.setData(productDetailBean);
		if (isAdd) {
			localFragmentTransaction1.add(
					ApplicationUtils.getResId("id", "container"),
					mDetailFragment, FRAGMENT_TAG_DETAIL);
			isAdd = false;
		} else {
			localFragmentTransaction1.replace(
					ApplicationUtils.getResId("id", "container"),
					mDetailFragment);
		}
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}

	}

	public void choosePara(ProductDetailBean productDetailBeans) {
		FragmentTransaction localFragmentTransaction1 = localFragmentManager
				.beginTransaction();
		if (mParaFragment == null) {
			mParaFragment = new ProductParameterFragment();
		}
		if (productDetailBean != null)
			mParaFragment.setData(productDetailBean);
		localFragmentTransaction1.replace(
				ApplicationUtils.getResId("id", "container"), mParaFragment,
				FRAGMENT_TAG_PARA);
		if (!(localFragmentTransaction1.isEmpty())) {
			localFragmentTransaction1.commitAllowingStateLoss();
			getSupportFragmentManager().executePendingTransactions();
		}
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
				return (SpringAndroidService.getInstance(getApplication())
						.handleProtect(actionUrl, formData,
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
						if (HP_User.getOnLineUserId(mContext) == 0) {

						}
					}
				} else if (exception instanceof MissingAuthorizationException) {
					// go login
					if (HP_User.getOnLineUserId(mContext) == 0) {

					}
				}
			} else if (exception == null) {
				if ((!TextUtils.isEmpty(result.toString()))
						&& (!result.toString().equals("null"))) {
					// ToastUtil.show(mContext, ApplicationUtils.getResId(
					// "string", "shop_addcart_suc"), Toast.LENGTH_SHORT);
					CartItemBean cib = new CartItemBean();
					cib.setId(productDetailBean.getId());
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
					if (total > 0) {
						cartCount.setVisibility(View.VISIBLE);
						cartCount.setText(String.valueOf(total));
						Animation shake = AnimationUtils
								.loadAnimation(mContext, ApplicationUtils
										.getResId("anim", "shop_shake"));
						cartCount.startAnimation(shake);
					} else {
						cartCount.setVisibility(View.INVISIBLE);
					}

				} else {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "shop_addcart_fail"), Toast.LENGTH_SHORT);
				}

			}

		}
	}

	@Override
	public void onChange() {
		new myGetCartTask(null, null, mContext, this).execute();
	}

	class myGetCartTask extends GetCartTask {

		public myGetCartTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext,
				Activity activity) {
			super(actionUrl, formData, mContext, activity);
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			int count = MyApplication.getCartNum();
			if (count > 0) {
				cartCount.setVisibility(View.VISIBLE);
				cartCount.setText(String.valueOf(count));
				// Animation shake = AnimationUtils.loadAnimation(mContext,
				// ApplicationUtils.getResId("anim", "shop_shake"));
				// cartCount.startAnimation(shake);
			} else {
				cartCount.setVisibility(View.INVISIBLE);
			}

		}

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}
}
