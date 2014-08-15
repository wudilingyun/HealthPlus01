/**
 * 
 */
package com.vee.shop.bean;

import java.io.Serializable;

public class ImageBean implements Serializable, Comparable<ImageBean> {

	private static final long serialVersionUID = 2690182085466121899L;
	private String productid;
	private String type;
	private String imageurl;
	private String productimageid;
	private int sequence;

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getProductimageid() {
		return productimageid;
	}

	public void setProductimageid(String productimageid) {
		this.productimageid = productimageid;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Override
	public int compareTo(ImageBean obj) {
		return this.sequence - obj.sequence;
	}

}
