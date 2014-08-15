package com.vee.shop.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItemBean implements Parcelable {
	private String id;
	private String imgUrl;
	private String name;
	private String price;
	private String count;
    private String availablecount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getAvailablecount() {
		return availablecount;
	}

	public void setAvailablecount(String availablecount) {
		this.availablecount = availablecount;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(imgUrl);
		dest.writeString(name);
		dest.writeString(price);
		dest.writeString(count);
		dest.writeString(availablecount);
	}

	public static final Parcelable.Creator<CartItemBean> CREATOR = new Creator<CartItemBean>() {

		@Override
		public CartItemBean createFromParcel(Parcel source) {
			CartItemBean cb = new CartItemBean();
			cb.setId(source.readString());
			cb.setImgUrl(source.readString());
			cb.setName(source.readString());
			cb.setPrice(source.readString());
			cb.setCount(source.readString());
			cb.setAvailablecount(source.readString());
			return cb;
		}

		@Override
		public CartItemBean[] newArray(int size) {
			return new CartItemBean[size];
		}

	};
}
