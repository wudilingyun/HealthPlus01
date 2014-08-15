package com.vee.myhealth.bean;

import java.io.Serializable;

public class TZtest  implements Serializable{
	private  String question;
	private String astersk;//判断是否带*号  0 不带。1 带
	private  String repeat;//判断是否需要重复计算选项 0 不需要  其他值为对应分类需要计算的值
	private  String repeat_astersk;//重复计算的得分是否带*号
	private String h_id;
	private String num;
	public String getAstersk() {
		return astersk;
	}
	public void setAstersk(String astersk) {
		this.astersk = astersk;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getRepeat_astersk() {
		return repeat_astersk;
	}
	public void setRepeat_astersk(String repeat_astersk) {
		this.repeat_astersk = repeat_astersk;
	}
	public String getH_id() {
		return h_id;
	}
	public void setH_id(String h_id) {
		this.h_id = h_id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	
	
}
