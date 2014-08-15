package com.vee.healthplus.heahth_news_beans;

public class Doc {
	
	
	public Doc() {
		super();
	}
	private String title;
	private String summary;
	private String imgUrl;
	private String content;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	@Override
	public String toString() {
		return "Doc [title=" + title + ", summary=" + summary + ", imgUrl="
				+ imgUrl + "]";
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
