package com.vee.shop.bean;

import java.io.Serializable;

public class StationBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2957508617902664997L;
	private String location;
	private String authcode;
	private String name;
	private String address;
	private String phone;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAuthcode() {
		return authcode;
	}
	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
