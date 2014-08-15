package com.vee.shop.bean;

import java.io.Serializable;
import java.util.List;

public class TotalPointBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4737203860213182667L;
	private int totalscore;
	private List<OrderPointBean> clientorderscorelist;

	public int getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}

	public List<OrderPointBean> getClientorderscorelist() {
		return clientorderscorelist;
	}

	public void setClientorderscorelist(
			List<OrderPointBean> clientorderscorelist) {
		this.clientorderscorelist = clientorderscorelist;
	}

}
