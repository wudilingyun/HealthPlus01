package com.vee.shop.alipay.beans;

public class Order {
	/**
	 * 订单号
	 */
	public String outTradeno;
	/**
	 * 订单描述
	 */
	public String orderDesc;
	/**
	 * 总金额
	 */
	public double totalFee;
	/**
	 * 订单标题
	 */
	public String orderTitle;

	public Order() {
	}

	public Order(String outTradeno, String orderDesc, double totalFee,
			String orderTitle) {
		super();
		this.outTradeno = outTradeno;
		this.orderDesc = orderDesc;
		this.totalFee = totalFee;
		this.orderTitle = orderTitle;
	}

	public String getStringOrderFee() {
		return String.valueOf(totalFee);
	}
}
