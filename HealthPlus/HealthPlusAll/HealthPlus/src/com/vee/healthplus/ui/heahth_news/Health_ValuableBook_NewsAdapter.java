package com.vee.healthplus.ui.heahth_news;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_beans.Doc;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.RoundImageView;
import com.yunfox.s4aservicetest.response.Exam;
import com.yunfox.s4aservicetest.response.ExamType;
import com.yunfox.s4aservicetest.response.YysNewsResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Health_ValuableBook_NewsAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<YysNewsResponse> newslist;
	private ImageLoader imageLoader;
	private List<Bitmap> imgbitmap;// 要加载的图片
	private Context context;
	List<Bitmap> bitmaps;
	public Health_ValuableBook_NewsAdapter(Context context,
			ImageLoader imageLoader) {
		super();
		inflater = LayoutInflater.from(context);
		newslist = new ArrayList<YysNewsResponse>();
		this.context = context;
		this.imageLoader = imageLoader;
	}

	public void listaddAdapter(List<YysNewsResponse> newslist) {
		
		this.newslist.addAll(newslist);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newslist.size();
	}

	@Override
	public YysNewsResponse getItem(int position) {
		// TODO Auto-generated method stub
		return newslist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder v;
		//View view = null;
		if (convertView != null) {
			v = (ViewHolder) convertView.getTag();

		} else {

			convertView = (View) inflater.inflate(R.layout.health_valuablebook_item,
					parent, false);

			v = new ViewHolder();

			v.newstitle = (TextView) convertView.findViewById(R.id.newstitle);
			// v.newsbrief = (TextView) view.findViewById(R.id.newsbrief);
			v.imageView_healthnews = (RoundImageView) convertView
					.findViewById(R.id.imageView_healthnews);
			v.newstime = (TextView)convertView.findViewById(R.id.data);
		
			convertView.setTag(v);
		}

		v.newstitle.setText(newslist.get(position).getSubject());
		// v.newsbrief.setText(newslist.get(position).getSummary());
		v.newstitle.setTag(newslist.get(position).getNewsurl());
		System.out.println("webview的网址是"+newslist.get(position).getNewsurl());
		long creattime = newslist.get(position).getCreatetime().getTime();
		SimpleDateFormat sdf= new SimpleDateFormat("MM/dd");  
		v.imageView_healthnews.setImageResource(R.drawable.news_img_default);
		v.newstime.setText(sdf.format(creattime));
		v.newstime.setTag(newslist.get(position).getDigest().toString());
	
		String imgurl = newslist.get(position).getThumbnailurl();
		final String tmpImageUrl = imgurl;
		System.out.println("final"+tmpImageUrl+"position"+imgurl);
			imageLoader.addTask(imgurl, v.imageView_healthnews);
		return convertView;
	}

	public class ViewHolder {

		private int position;
		private TextView newstitle, newsbrief,newstime;
		private ImageView imageView_top ;
		private RoundImageView imageView_healthnews;
	}

}
