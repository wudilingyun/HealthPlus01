package com.vee.shop.bean;

import java.io.Serializable;
import java.util.List;

public class OrderBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7284465804238288044L;
	private String returncode;
	private String returndesc;
	private String id;
	private String receiver;
	private String mobile;
	private String address;
	private String receivetime;
	private String invoice;
	private String totalprice;
	private String invoiceName;
	private String ordernumber;
	private List<CartItemBean> cartItemList;
	public List<CartItemBean> getCartItemList() {
		return cartItemList;
	}
	public void setCartItemList(List<CartItemBean> cartItemList) {
		this.cartItemList = cartItemList;
	}
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	public String getReturndesc() {
		return returndesc;
	}
	public void setReturndesc(String returndesc) {
		this.returndesc = returndesc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getReceivetime() {
		return receivetime;
	}
	public void setReceivetime(String receivetime) {
		this.receivetime = receivetime;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}
	public String getInvoiceName() {
		return invoiceName;
	}
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}
	public String getOrdernumber() {
		return ordernumber;
	}
	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}
	
}
