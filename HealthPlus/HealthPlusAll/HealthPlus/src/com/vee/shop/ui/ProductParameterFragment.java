package com.vee.shop.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.shop.bean.ParameterBean;
import com.vee.shop.bean.ProductDetailBean;
import com.vee.shop.util.ApplicationUtils;

public class ProductParameterFragment extends BaseFragment {
	private static final String TAG = "ProductParameterFragment";
	private ProductDetailBean pdBean;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(
				R.layout.shop_productparameter_fragment, container, false);
		LinearLayout contentLayout = (LinearLayout) localView
				.findViewById(R.id.shop_productparameter_content);
		contentLayout.removeAllViews();
		ArrayList<ParameterBean> paraList = pdBean.getParaList();
		if (null != paraList) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, ApplicationUtils.dip2px(mContext, 10),
					0, 0);
			for (int i = 0; i < paraList.size(); i++) {
				TextView tv = new TextView(mContext);
				tv.setLayoutParams(layoutParams);
				tv.setPadding(ApplicationUtils.dip2px(mContext, 5), 0, 0, 0);
				tv.setText(paraList.get(i).getParametername() + ":"
						+ paraList.get(i).getParametervalue());
				contentLayout.addView(tv);
			}
		}

		return localView;
	}

	public void setData(ProductDetailBean data) {
		this.pdBean = data;
	}
}
