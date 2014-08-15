package com.vee.shop.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class PointBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	private String ordernumber;
	private Float totalprice;
	private String productname;
	private int productid;
	private String imageurl;
	private String productdetail;
	private Float productprice;
	private int buycount;
	private int perproductscore;
	private int totalscore;
	private Timestamp createtime;
	private String memo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public Float getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Float totalprice) {
		this.totalprice = totalprice;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getProductdetail() {
		return productdetail;
	}

	public void setProductdetail(String productdetail) {
		this.productdetail = productdetail;
	}

	public Float getProductprice() {
		return productprice;
	}

	public void setProductprice(Float productprice) {
		this.productprice = productprice;
	}

	public int getBuycount() {
		return buycount;
	}

	public void setBuycount(int buycount) {
		this.buycount = buycount;
	}

	public int getPerproductscore() {
		return perproductscore;
	}

	public void setPerproductscore(int perproductscore) {
		this.perproductscore = perproductscore;
	}

	public int getTotalscore() {
		return totalscore;
	}

	public void setTotalscore(int totalscore) {
		this.totalscore = totalscore;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
