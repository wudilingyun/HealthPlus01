package com.vee.myhealth.bean;

public class Answer {
	private String first_score="1";
	private String second_score="2";
	private String third_score="3";
	private String four_score="4";
	private String five_score="5";
	
	private String first_response ="没有";
	private String second_response ="很少";
	private String third_response ="有时";
	private String four_response="经常";
	private String five_response="总是";


	public String getFirst_response() {
		return first_response;
	}

	public void setFirst_response(String first_response) {
		this.first_response = first_response;
	}

	public String getSecond_response() {
		return second_response;
	}

	public void setSecond_response(String second_response) {
		this.second_response = second_response;
	}

	public String getThird_response() {
		return third_response;
	}

	public void setThird_response(String third_response) {
		this.third_response = third_response;
	}

	public String getFour_response() {
		return four_response;
	}

	public void setFour_response(String four_response) {
		this.four_response = four_response;
	}

	public String getFive_response() {
		return five_response;
	}

	public void setFive_response(String five_response) {
		this.five_response = five_response;
	}

}
