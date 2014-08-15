package com.vee.myhealth.bean;

import java.io.Serializable;

public class HealthQuestionEntity implements Serializable{
	private int id;
	private String question;
	private String yesskip;
	private String noskip;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getYesskip() {
		return yesskip;
	}
	public void setYesskip(String yesskip) {
		this.yesskip = yesskip;
	}
	public String getNoskip() {
		return noskip;
	}
	public void setNoskip(String noskip) {
		this.noskip = noskip;
	}
	@Override
	public String toString() {
		return "HealthQuestionEntity [id=" + id + ", question=" + question
				+ ", yesskip=" + yesskip + ", noskip=" + noskip + "]";
	}

}
