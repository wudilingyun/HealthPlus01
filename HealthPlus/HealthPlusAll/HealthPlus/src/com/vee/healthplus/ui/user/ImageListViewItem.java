package com.vee.healthplus.ui.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.util.user.HP_User;

public class ImageListViewItem implements ListElement {

	private String name;
	private Bitmap photo;
	private TextView textView;
	private ImageView ivPhoto;
	private ImageLoader imageLoader;
	private HP_User user;
	private RelativeLayout layout;
	private Object tag;

	public ImageListViewItem(HP_User user, ImageLoader imageLoader) {
		this.user = user;
		this.imageLoader = imageLoader;
	}

	public ListElement setText(String text) {
		this.name = text;
		return this;
	}

	public ListElement setPhoto(Bitmap photo) {
		this.photo = photo;
		return this;
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.healthplus_personal_info_edit_image_item;
	}

	@Override
	public View getViewForListElement(LayoutInflater layoutInflater,
			Context context, View view) {
		// TODO Auto-generated method stub

		layout = (RelativeLayout) layoutInflater.inflate(
				getLayoutId(), null);
		textView = (TextView) layout
				.findViewById(R.id.health_plus_personal_info_edit_image_item_name_tv);
		layout.setTag(tag);
		textView.setText(name);
		ivPhoto = (ImageView) layout
				.findViewById(R.id.health_plus_personal_info_edit_image_item_photo_iv);
		if (photo != null) {
			ivPhoto.setImageBitmap(photo);
		} else {
			//if(user.photourl!=null&&!user.photourl.equals("")){
				imageLoader.addTask(user.photourl, ivPhoto);
			//}
		}
		return layout;
	}

	@Override
	public ListElement setValue(String value) {
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
		return null;
	}

	@Override
	public Bitmap getPhoto() {
		// TODO Auto-generated method stub
		return photo;
	}

	@Override
	public ListElement setTag(Object tag) {
		this.tag=tag;
		return this;
	}

}
