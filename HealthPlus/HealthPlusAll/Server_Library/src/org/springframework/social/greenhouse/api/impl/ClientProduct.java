package org.springframework.social.greenhouse.api.impl;

import java.io.Serializable;

public class ClientProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8750588432006251337L;
	
	private String productimgurl;
	private String producthotimgurl;
	private String producturl;
	private String title;
	private Float price;
	private String desc;
	private int productid;
	
	public String getProductimgurl() {
		return productimgurl;
	}
	public void setProductimgurl(String productimgurl) {
		this.productimgurl = productimgurl;
	}
	public String getProducturl() {
		return producturl;
	}
	public void setProducturl(String producturl) {
		this.producturl = producturl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the productid
	 */
	public int getProductid() {
		return productid;
	}
	/**
	 * @param productid the productid to set
	 */
	public void setProductid(int productid) {
		this.productid = productid;
	}
	/**
	 * @return the producthotimgurl
	 */
	public String getProducthotimgurl() {
		return producthotimgurl;
	}
	/**
	 * @param producthotimgurl the producthotimgurl to set
	 */
	public void setProducthotimgurl(String producthotimgurl) {
		this.producthotimgurl = producthotimgurl;
	}
}
