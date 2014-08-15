package com.vee.healthplus.ui.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;

public interface ListElement {
	public int getLayoutId();
	public View getViewForListElement(LayoutInflater layoutInflater,
	Context context, View view);
	public ListElement setPhoto(Bitmap photo);
	public ListElement setValue(String value);
	public ListElement setText(String text);
	public String getText();
	public String getValue();
	public Bitmap getPhoto();
	public ListElement setTag(Object tag);

}
