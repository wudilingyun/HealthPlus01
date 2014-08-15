package com.vee.healthplus.ui.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;

public class TextListViewItem implements ListElement {

	private String name;
	private String value;
	private TextView textView;
	private TextView valueView;
	private RelativeLayout layout;
	private Object tag;

	public ListElement setText(String text) {
		this.name = text;
		return this;
	}

	public ListElement setValue(String value) {
		this.value = value;
		return this;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.healthplus_personal_info_edit_text_item;

	}

	@Override
	public View getViewForListElement(LayoutInflater layoutInflater,
			Context context, View view) {
		// TODO Auto-generated method stub

		layout = (RelativeLayout) layoutInflater.inflate(getLayoutId(), null);
		layout.setTag(tag);
		textView = (TextView) layout
				.findViewById(R.id.health_plus_personal_info_edit_text_item_name_tv);
		valueView = (TextView) layout
				.findViewById(R.id.health_plus_personal_info_edit_text_item_value_tv);
		valueView.setText(value);
		textView.setText(name);
		return layout;
	}

	@Override
	public ListElement setPhoto(Bitmap photo) {
		// TODO Auto-generated method stub
		return this;

	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public Bitmap getPhoto() {
		// TODO Auto-generated method stub
		return null;
	}

	public ListElement setTag(Object tag) {
		this.tag=tag;
		return this;
	}

}
