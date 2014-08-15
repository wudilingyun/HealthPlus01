/**
 * 
 */
package com.vee.shop.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductDetailBean implements Serializable {

	private static final long serialVersionUID = 4465781088365863580L;
	private String name;
	private String price;
	private String originalprice;
	private String availablecount;
	private String id;
	private String addCharUrl;
	private String score;
	private ArrayList<ImageBean> imageList = new ArrayList<ImageBean>();
	private ArrayList<String> suitableList = new ArrayList<String>();
	private ArrayList<ParameterBean> paraList = new ArrayList<ParameterBean>();

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

	public String getOriginalprice() {
		return originalprice;
	}

	public void setOriginalprice(String originalprice) {
		this.originalprice = originalprice;
	}

	public String getAvailablecount() {
		return availablecount;
	}

	public void setAvailablecount(String availablecount) {
		this.availablecount = availablecount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddCharUrl() {
		return addCharUrl;
	}

	public void setAddCharUrl(String addCharUrl) {
		this.addCharUrl = addCharUrl;
	}

	public ArrayList<String> getSuitableList() {
		return suitableList;
	}

	public void addSuitableItem(String suitable) {
		this.suitableList.add(suitable);
	}

	public void setSuitableList(ArrayList<String> suitableList) {
		this.suitableList = suitableList;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public ArrayList<ImageBean> getImageList() {
		return imageList;
	}

	public void setImageList(ArrayList<ImageBean> imageList) {
		this.imageList = imageList;
	}

	public ArrayList<ParameterBean> getParaList() {
		return paraList;
	}

	public void setParaList(ArrayList<ParameterBean> paraList) {
		this.paraList = paraList;
	}

}
