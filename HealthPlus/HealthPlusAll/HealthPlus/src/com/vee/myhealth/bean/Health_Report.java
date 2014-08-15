package com.vee.myhealth.bean;

public class Health_Report {

	private String name;
	private String feature;
	private String body_feature;
	private String expression;
	private String heart_feature;
	private String easy_sicken;
	private String environment_ataptation;
	private String bite_sup;
	private String live;
	private String sport;
	private String interest;
	private String medicine;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getBody_feature() {
		return body_feature;
	}

	public void setBody_feature(String body_feature) {
		this.body_feature = body_feature;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getHeart_feature() {
		return heart_feature;
	}

	public void setHeart_feature(String heart_feature) {
		this.heart_feature = heart_feature;
	}

	public String getEasy_sicken() {
		return easy_sicken;
	}

	public void setEasy_sicken(String easy_sicken) {
		this.easy_sicken = easy_sicken;
	}

	public String getEnvironment_ataptation() {
		return environment_ataptation;
	}

	public void setEnvironment_ataptation(String environment_ataptation) {
		this.environment_ataptation = environment_ataptation;
	}

	public String getBite_sup() {
		return bite_sup;
	}

	public void setBite_sup(String bite_sup) {
		this.bite_sup = bite_sup;
	}

	public String getLive() {
		return live;
	}

	public void setLive(String live) {
		this.live = live;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	@Override
	public String toString() {
		return "Health_Report [name=" + name + ", feature=" + feature
				+ ", body_feature=" + body_feature + ", expression="
				+ expression + ", heart_feature=" + heart_feature
				+ ", easy_sicken=" + easy_sicken + ", environment_ataptation="
				+ environment_ataptation + ", bite_sup=" + bite_sup + ", live="
				+ live + ", sport=" + sport + ", interest=" + interest
				+ ", medicine=" + medicine + "]";
	}
	
	
	
}
