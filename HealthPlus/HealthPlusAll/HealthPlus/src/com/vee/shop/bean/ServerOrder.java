package com.vee.shop.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class ServerOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7220701592522968186L;

	private int orderid;
	private String username;
	private int status;
	private String imgurl;
	private Timestamp orderdate;
	private Float totalprice;
	private String detailurl;
	private String mobile;
	private String receiver;
	private String address;
	private int receivetime;
	private int fapiao;
	private String ordernumber;
	private String danweifapiaoname;
	private int cancel;
	private int paytype;

	public int getPaytype() {
		return paytype;
	}

	public void setPaytype(int paytype) {
		this.paytype = paytype;
	}

	/**
	 * @return the orderid
	 */
	public int getOrderid() {
		return orderid;
	}

	/**
	 * @param orderid
	 *            the orderid to set
	 */
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the imgurl
	 */
	public String getImgurl() {
		return imgurl;
	}

	/**
	 * @param imgurl
	 *            the imgurl to set
	 */
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	/**
	 * @return the orderdate
	 */
	public Timestamp getOrderdate() {
		return orderdate;
	}

	/**
	 * @param orderdate
	 *            the orderdate to set
	 */
	public void setOrderdate(Timestamp orderdate) {
		this.orderdate = orderdate;
	}

	/**
	 * @return the totalprice
	 */
	public Float getTotalprice() {
		return totalprice;
	}

	/**
	 * @param totalprice
	 *            the totalprice to set
	 */
	public void setTotalprice(Float totalprice) {
		this.totalprice = totalprice;
	}

	/**
	 * @return the detailurl
	 */
	public String getDetailurl() {
		return detailurl;
	}

	/**
	 * @param detailurl
	 *            the detailurl to set
	 */
	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver
	 *            the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the receivetime
	 */
	public int getReceivetime() {
		return receivetime;
	}

	/**
	 * @param receivetime
	 *            the receivetime to set
	 */
	public void setReceivetime(int receivetime) {
		this.receivetime = receivetime;
	}

	/**
	 * @return the fapiao
	 */
	public int getFapiao() {
		return fapiao;
	}

	/**
	 * @param fapiao
	 *            the fapiao to set
	 */
	public void setFapiao(int fapiao) {
		this.fapiao = fapiao;
	}

	/**
	 * @return the ordernumber
	 */
	public String getOrdernumber() {
		return ordernumber;
	}

	/**
	 * @param ordernumber
	 *            the ordernumber to set
	 */
	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	/**
	 * @return the danweifapiaoname
	 */
	public String getDanweifapiaoname() {
		return danweifapiaoname;
	}

	/**
	 * @param danweifapiaoname
	 *            the danweifapiaoname to set
	 */
	public void setDanweifapiaoname(String danweifapiaoname) {
		this.danweifapiaoname = danweifapiaoname;
	}

	/**
	 * @return the cancel
	 */
	public int getCancel() {
		return cancel;
	}

	/**
	 * @param cancel
	 *            the cancel to set
	 */
	public void setCancel(int cancel) {
		this.cancel = cancel;
	}
}
