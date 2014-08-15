package com.vee.shop.bean;

import java.util.ArrayList;

public class OrderDetailBean extends OrderBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 740452428601753716L;
	private String status;
	private String orderdate;
	private ArrayList<CartItemBean> productList = new ArrayList<CartItemBean>();
	private String paytype;

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public ArrayList<CartItemBean> getProductList() {
		return productList;
	}

	public void setProductList(ArrayList<CartItemBean> productList) {
		this.productList = productList;
	}

}
