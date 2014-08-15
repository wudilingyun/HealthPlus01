package com.vee.shop.activity;

import java.io.File;
import java.util.List;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.util.user.HP_User;
import com.vee.shop.bean.ProductDetailBean;
import com.vee.shop.http.PublicGetTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.ui.ProductDetailFragment;
import com.vee.shop.ui.ProductDetailFragment.OnCartAddedListener;
import com.vee.shop.ui.ProductParameterFragment;
import com.vee.shop.ui.ShopHeaderView;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;

public class ProductDetailActivity extends BaseActivity implements
		OnClickListener, OnCartAddedListener {

	private static final String TAG = "ProductDetailActivity";

	private ShopHeaderView hv;
	private ImageView ivUp;
	private ImageView leftBtn;
	private TextView cartCount;
	private ImageView rightBtn;
	private ViewPager viewPager;
	private RadioButton product_tab_detail, product_tab_parameter;

	private String mProductUrl;
	private String mProductId;
	private String backupJson;
	private ProductDetailBean productDetailBean = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_productdetail_activity);
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

	@SuppressLint("WrongViewCast")
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
		product_tab_detail = (RadioButton) findViewById(R.id.productdetail_tab_detail);
		product_tab_detail.setOnClickListener(this);
		product_tab_parameter = (RadioButton) findViewById(R.id.productdetail_tab_parameter);
		product_tab_parameter.setOnClickListener(this);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int id) {
				switch (id) {
				case 0:
					product_tab_detail.setChecked(true);
					break;
				case 1:
					product_tab_parameter.setChecked(true);
					break;

				default:
					break;
				}
			}

		});

	}

	private boolean handleIntent() {
		Intent localIntent = getIntent();
		this.mProductUrl = localIntent.getStringExtra("product_url");
		this.mProductId = localIntent.getStringExtra("product_id");
		return (!TextUtils.isEmpty(mProductUrl));
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
		case R.id.productdetail_tab_detail:
			viewPager.setCurrentItem(0);
			break;
		case R.id.productdetail_tab_parameter:
			viewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}

	class FragmentAdapter extends FragmentPagerAdapter {
		private ProductDetailBean pdBean;

		public FragmentAdapter(FragmentManager fm) {
			super(fm);

		}

		public FragmentAdapter(FragmentManager fm, ProductDetailBean pdBean) {
			super(fm);
			this.pdBean = pdBean;
		}

		@Override
		public Fragment getItem(int id) {
			switch (id) {
			case 0:
				ProductDetailFragment detailFragment = new ProductDetailFragment();
				detailFragment.setData(pdBean);
				return detailFragment;
			case 1:
				ProductParameterFragment parameterFragment = new ProductParameterFragment();
				parameterFragment.setData(pdBean);
				return parameterFragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
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
				editor.putString("ProductdetailJsonString" + mProductId, result);
				editor.commit();
				json = result;
			} else {
				json = backupJson;
			}
			productDetailBean = httpUtil.parseProductDetail(json);
			if (null != productDetailBean) {
				FragmentAdapter adapter = new FragmentAdapter(
						getSupportFragmentManager(), productDetailBean);
				viewPager.setAdapter(adapter);
				refreshUI();
			}
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
	}

	@Override
	public void OnCartAdded(int num) {
		int count = MyApplication.getCartNum();
		if (count > 0) {
			cartCount.setVisibility(View.VISIBLE);
			cartCount.setText(String.valueOf(count));
			Animation shake = AnimationUtils.loadAnimation(this,
					ApplicationUtils.getResId("anim", "shop_shake"));
			cartCount.startAnimation(shake);
		} else {
			cartCount.setVisibility(View.INVISIBLE);
		}
	}

}
