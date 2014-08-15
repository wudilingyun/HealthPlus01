package com.vee.shop.bean;

import java.io.Serializable;
import java.util.List;

public class OrderPointBean implements Serializable {

	private static final long serialVersionUID = 732841945400278488L;
	private String ordernumber;
	private int orderscore;
	private List<PointBean> scoreloglist;

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public int getOrderscore() {
		return orderscore;
	}

	public void setOrderscore(int orderscore) {
		this.orderscore = orderscore;
	}

	public List<PointBean> getScoreloglist() {
		return scoreloglist;
	}

	public void setScoreloglist(List<PointBean> scoreloglist) {
		this.scoreloglist = scoreloglist;
	}

}
