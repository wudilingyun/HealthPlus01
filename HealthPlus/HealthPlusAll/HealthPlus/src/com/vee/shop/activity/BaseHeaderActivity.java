package com.vee.shop.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.widget.HeaderView;
import com.vee.shop.ui.ShopHeaderView;
import com.vee.shop.ui.ShopHeaderView.OnHeaderClickListener;

public class BaseHeaderActivity extends BaseActivity {

	private FrameLayout mContent;

	private ShopHeaderView hv;

	private ImageView leftBtn;

	private ImageView rightBtn;

	private TextView tvCartCount;

	private OnHeaderClickListener headerClickListener = new OnHeaderClickListener() {

		@Override
		public void OnHeaderClick(View v, int option) {
			// TODO Auto-generated method stub
			if (option == ShopHeaderView.HEADER_CART) {
				// Intent intent = new Intent();
				// intent.setClass(BaseHeaderActivity.this, CartActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// startActivity(intent);
			} else if (option == HeaderView.HEADER_BACK) {
				finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.shop_base_header_activity);
		mContent = (FrameLayout) findViewById(R.id.container);
		hv = (ShopHeaderView) findViewById(R.id.header);
		leftBtn = (ImageView) findViewById(R.id.header_lbtn_img);
		rightBtn = (ImageView) findViewById(R.id.header_rbtn_img);
		tvCartCount = (TextView) findViewById(R.id.shop_title_bar_shopping_count);
		hv.setOnHeaderClickListener(headerClickListener);
	}

	public void resetHeaderClickListener() {
		hv.setOnHeaderClickListener(headerClickListener);
	}

	public void setHeaderClickListener(OnHeaderClickListener lstn) {
		hv.setOnHeaderClickListener(lstn);
	}

	public void setContainer(View v) {
		mContent.removeAllViews();
		mContent.addView(v);
	}

	public void setLeftBtnType(int type) {
		hv.setLeftOption(type);
	}

	public void setLeftBtnVisible(int visibility) {
		leftBtn.setVisibility(visibility);
	}

	public void setRightBtnVisible(int visibility) {
		rightBtn.setVisibility(visibility);
	}

	public void setCountVisible(int visibility) {
		tvCartCount.setVisibility(visibility);
	}

	public ShopHeaderView getHeaderView() {
		return hv;
	}

	public void updateCartCount(int count) {
		if (count > 0) {
			tvCartCount.setVisibility(View.VISIBLE);
			tvCartCount.setText(String.valueOf(count));
		} else {
			tvCartCount.setVisibility(View.GONE);
		}
	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (KeyEvent.KEYCODE_BACK == keyCode) {
	// return true;
	// }
	// return false;
	// }
	public void updateHeaderTitle(String title) {
		hv.setHeaderTitle(title);
	}

	public void setLeftBtnClickListenter(OnClickListener listener) {
		leftBtn.setOnClickListener(listener);
	}

	public void setRightBtnClickListenter(OnClickListener listener) {
		rightBtn.setOnClickListener(listener);
	}
}
