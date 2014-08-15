package com.vee.shop.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressBean implements Parcelable {
	private String id;
	private String province;
	private String city;
	private String district;
	private String detail;
    private String receiver;
    private String postcode;
    private String mobile;
    private String deleteUrl;
    private String updateUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}
	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}
	public String getUpdateUrl() {
		return updateUrl;
	}
	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(province);
		dest.writeString(city);
		dest.writeString(district);
		dest.writeString(detail);
		dest.writeString(receiver);
		dest.writeString(postcode);
		dest.writeString(mobile);
		dest.writeString(deleteUrl);
		dest.writeString(updateUrl);
	}
    
	public static final Parcelable.Creator<AddressBean> CREATOR = new Creator<AddressBean>() {

		@Override
		public AddressBean createFromParcel(Parcel source) {
			AddressBean ab = new AddressBean();
			ab.setId(source.readString());
			ab.setProvince(source.readString());
			ab.setCity(source.readString());
			ab.setDistrict(source.readString());
			ab.setDetail(source.readString());
			ab.setReceiver(source.readString());
			ab.setPostcode(source.readString());
			ab.setMobile(source.readString());
			ab.setDeleteUrl(source.readString());
			ab.setUpdateUrl(source.readString());
			return ab;
		}

		@Override
		public AddressBean[] newArray(int size) {
			return new AddressBean[size];
		}

	};
}
