package com.vee.healthplus.heahth_news_utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vee.healthplus.heahth_news_beans.FeedComment;

import android.content.Context;
import android.text.TextUtils;

public class JsonComment {

	// 状态评论
	private static final String FEEDCOMMENT = "feedcomment.json";

	/**
	 * 解析状态评论
	 * 
	 * @param context
	 * @param comments
	 * @return
	 */
	public static boolean resoleFeedComment(Context context,
			List<FeedComment> comments) {
		//获得JSon数据   并且解析
		String json ="abcded" ;
		if (json != null) {
			try {
				JSONArray array = new JSONArray(json);
				FeedComment comment = null;
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					String name = object.getString(FeedComment.NAME);
					String avatar = object.getString(FeedComment.AVATAR);
					String content = object.getString(FeedComment.CONTENT);
					String time = object.getString(FeedComment.TIME);
					comment = new FeedComment(name, avatar, content, time);
					comments.add(comment);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				comments = null;
				return false;
			}
			return true;
		}
		return false;
	}
}
