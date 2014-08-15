package com.vee.shop.bean;

import java.io.Serializable;

public class CartAddress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6312344401815622412L;

	private String province;
	private String city;
	private String district;
	private String detail;
	private String receiver;
	private String postcode;
	private String mobile;
	private int cartaddressid;
	private String deleteurl;
	private String updateurl;
	
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
	/**
	 * @return the cartaddressid
	 */
	public int getCartaddressid() {
		return cartaddressid;
	}
	/**
	 * @param cartaddressid the cartaddressid to set
	 */
	public void setCartaddressid(int cartaddressid) {
		this.cartaddressid = cartaddressid;
	}
	/**
	 * @return the deleteurl
	 */
	public String getDeleteurl() {
		return deleteurl;
	}
	/**
	 * @param deleteurl the deleteurl to set
	 */
	public void setDeleteurl(String deleteurl) {
		this.deleteurl = deleteurl;
	}
	/**
	 * @return the updateurl
	 */
	public String getUpdateurl() {
		return updateurl;
	}
	/**
	 * @param updateurl the updateurl to set
	 */
	public void setUpdateurl(String updateurl) {
		this.updateurl = updateurl;
	}
}
