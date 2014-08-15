package com.vee.myhealth.bean;

import java.io.Serializable;

public class ResultEntity implements Serializable{
	private String  title;
	private String  name;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
