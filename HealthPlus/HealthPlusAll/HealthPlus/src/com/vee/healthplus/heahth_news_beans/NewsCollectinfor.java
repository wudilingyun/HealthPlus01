package com.vee.healthplus.heahth_news_beans;

public class NewsCollectinfor {
	private String title;
	private String imgurl;
	private String weburl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getWeburl() {
		return weburl;
	}

	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

	@Override
	public String toString() {
		return "NewsCollectinfor [title=" + title + ", imgurl=" + imgurl
				+ ", weburl=" + weburl + "]";
	}

}
