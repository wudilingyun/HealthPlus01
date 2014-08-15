package com.vee.imandforum.heahth_news_http;

public class Contact {

	public static String HealthNES_URL = "http://bk.browser.mobifox.cn:6060/solr/healthnews/select?q=isShow:"
			+ 2
			+ "&wt=json&fl=title,summary,imgUrl,premiereDate&rows="
			+ "20"
			+ "&sort=premiereDate%20desc";
	public static String HealthNES_URL_1 = "http://bk.browser.mobifox.cn:6060/solr/healthnews/select?q=isShow:"
			+ 3
			+ "&wt=json&fl=title,summary,imgUrl,premiereDate&rows="
			+ "20"
			+ "&sort=premiereDate%20desc";

	public static String HealthNES_URL_a = "http://bk.browser.mobifox.cn:6060/solr/healthnews/select?q=isShow:"
			+ 2 + "&wt=json&fl=title,summary,imgUrl,premiereDate&rows=";

	public static String HealthNES_URL_b = "&sort=premiereDate%20desc";

	public static String HEALTHNES_IMG_URL = "http://bk.browser.mobifox.cn:6060";

	public static String HEALTHNEW_Content_URL_1 = "http://bk.browser.mobifox.cn:6060/solr/healthnews/select?q=imgUrl:%22";

	public static String HEALTHNEW_Content_URL_2 = "%22&wt=json&fl=imgUrl,content";
}
