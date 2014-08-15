package com.vee.healthplus.ui.heahth_news;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_beans.Doc;
import com.vee.healthplus.heahth_news_beans.FeedComment;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.yunfox.s4aservicetest.response.Exam;
import com.yunfox.s4aservicetest.response.ExamType;
import com.yunfox.s4aservicetest.response.NewsComment;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Health_ValueBook_Comment_Adapter extends BaseAdapter {

	LayoutInflater inflater;
	List<NewsComment> newslist;
	private ImageLoader imageLoader;
	private List<Bitmap> imgbitmap;// 要加载的图片
	private Context context;
	List<Bitmap> bitmaps;

	public Health_ValueBook_Comment_Adapter(Context context,
			ImageLoader imageLoader) {
		super();
		inflater = LayoutInflater.from(context);
		newslist = new ArrayList<NewsComment>();
		this.context = context;
		this.imageLoader = imageLoader;
	}

	public void listaddAdapter(List<NewsComment> newslist) {
		//this.newslist.clear();
		this.newslist.addAll(newslist);
	}
	
	public void listaddAllAdapter(List<NewsComment> newslist) {
		this.newslist.clear();
		this.newslist.addAll(newslist);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newslist.size();
	}

	@Override
	public NewsComment getItem(int position) {
		return newslist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = null;
		if (convertView != null) {
			view = convertView;

		} else {

			view = (View) inflater.inflate(
					R.layout.health_valuablebook_comment_item, parent, false);

			ViewHolder v = new ViewHolder();

			v.content = (TextView) view
					.findViewById(R.id.feedcomment_item_etv_content);
			v.name = (TextView) view
					.findViewById(R.id.feedcomment_item_etv_name);
			v.imghead = (ImageView) view
					.findViewById(R.id.feedcomment_item_iv_avatar);
			v.time = (TextView) view
					.findViewById(R.id.feedcomment_item_htv_time);
			view.setTag(v);
		}

		ViewHolder v = (ViewHolder) view.getTag();
		v.content.setText(newslist.get(position).getCommentcontent());
		v.name.setText(newslist.get(position).getAccountname());
		SimpleDateFormat sdft = new SimpleDateFormat("HH:mm:ss");
		v.time.setText(sdft.format(newslist.get(position).getCreatetime()));
		String imgurl = newslist.get(position).getAccountavatarurl();
		v.imghead.setImageResource(R.drawable.ic_common_def_header);
		imageLoader.addTask(imgurl, v.imghead);
		return view;
	}

	public class ViewHolder {

		private int position;
		private TextView content, name, time;
		private ImageView imghead;
	}

}
