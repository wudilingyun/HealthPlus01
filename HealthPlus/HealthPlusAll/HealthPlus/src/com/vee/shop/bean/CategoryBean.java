/**
 * 
 */
package com.vee.shop.bean;

import java.io.Serializable;

/**
 * @author Felix
 *
 */
public class CategoryBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4698594569014494511L;
	private String name;
	private String url;
	private String imgUrl;
	private String id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
