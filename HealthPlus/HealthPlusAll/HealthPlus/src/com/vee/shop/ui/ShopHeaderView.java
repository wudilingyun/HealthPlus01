package com.vee.shop.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;

public class ShopHeaderView extends LinearLayout implements OnClickListener {
	public static final int HEADER_NONE = 0;
	public static final int HEADER_BACK = 1;
	public static final int HEADER_CART = 2;

	private Integer mLOption;
	private Integer mROption;
	private TextView mHeaderTitle;
	private ImageView lbtnImg;
	private ImageView rbtnImg;
	private RelativeLayout relativeLayout;

	private OnHeaderClickListener mHeaderClickListener;

	public ShopHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	void initView(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ShopHeaderView, 0, 0);
		String headerText = a
				.getString(R.styleable.ShopHeaderView_shopheaderText);
		Integer headerLeftBtn = a.getInteger(
				R.styleable.ShopHeaderView_shopheaderLeftBtn, 0);
		Integer headerRightBtn = a.getInteger(
				R.styleable.ShopHeaderView_shopheaderRightBtn, 0);

		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.shop_header_view_new, this);

		lbtnImg = (ImageView) findViewById(R.id.header_lbtn_img);
		lbtnImg.setOnClickListener(this);

		relativeLayout = (RelativeLayout) findViewById(R.id.header_view_layout);

		switch (headerLeftBtn) {
		case 0:
			lbtnImg.setVisibility(View.INVISIBLE);
			break;

		case 1:
			mLOption = HEADER_BACK;
			break;
		default:
			break;
		}

		mHeaderTitle = (TextView) findViewById(R.id.header_text);
		mHeaderTitle.setText(headerText);

		rbtnImg = (ImageView) findViewById(R.id.header_rbtn_img);
		rbtnImg.setOnClickListener(this);

		switch (headerRightBtn) {
		case 0:
			rbtnImg.setVisibility(View.INVISIBLE);
			break;

		case 1:
			mROption = HEADER_CART;
			break;
		default:
			break;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.header_lbtn_img:
			if (null != mHeaderClickListener) {
				mHeaderClickListener.OnHeaderClick(this, mLOption);
			}
			break;

		case R.id.header_rbtn_img:
			if (null != mHeaderClickListener) {
				mHeaderClickListener.OnHeaderClick(this, mROption);
			}
			break;

		default:
			break;
		}
	}

	public static interface OnHeaderClickListener {
		public void OnHeaderClick(View v, int option);
	}

	public void setOnHeaderClickListener(OnHeaderClickListener l) {
		mHeaderClickListener = l;
	}

	public void setHeaderTitle(String title) {
		mHeaderTitle.setText(title);
	}

	public void setBackGroundColor(int colorId) {
		relativeLayout.setBackgroundResource(0);
		relativeLayout.setBackgroundColor(getResources().getColor(colorId));
	}

	public void setHeaderTitleColor(int colorId) {
		mHeaderTitle.setShadowLayer(0, 0, 0, 0);
		mHeaderTitle.setTextColor(getResources().getColor(colorId));
	}

	public void setLeftOption(int type) {
		mLOption = type;
	}

	public void setRightOption(int type) {
		mROption = type;
	}
}
