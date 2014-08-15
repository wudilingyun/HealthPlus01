package com.vee.shop.bean;

import java.io.Serializable;

public class ProductBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7884894861789289288L;
	private String imgUrl;
	private String url;
	private String name;
	private String price;
	private String desc;
	private String id;
	private String hotImgUrl;
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHotImgUrl() {
		return hotImgUrl;
	}
	public void setHotImgUrl(String hotImgUrl) {
		this.hotImgUrl = hotImgUrl;
	}
	
}
