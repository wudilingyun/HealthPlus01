package com.vee.moments;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.social.greenhouse.api.Profile;
import org.springframework.util.MultiValueMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.UserIndexUtils;
import com.vee.healthplus.util.user.UserInfoUtil;
import com.yunfox.s4aservicetest.response.Friend;
import com.yunfox.s4aservicetest.response.Moments;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class UserMomentsActivity extends FragmentActivity implements
		OnClickListener {

	private MomentsAdapter momentsAdapter;
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_my_moments, null);
		setContentView(view);

		ImageView imageViewMyDetail = (ImageView) findViewById(R.id.mydetail);
		ImageView sex_txt = (ImageView) findViewById(R.id.sex_txt);
		TextView textViewMyName = (TextView) view
				.findViewById(R.id.textViewUsername);
		TextView age_txt = (TextView) view.findViewById(R.id.age_txt);
		TextView weight_txt = (TextView) view.findViewById(R.id.weight_txt);

		ListView listViewMomentsList = (ListView) findViewById(R.id.momentslist);

		momentsAdapter = new MomentsAdapter(UserMomentsActivity.this);
		listViewMomentsList.setAdapter(momentsAdapter);

		Intent intent = getIntent();
		int friendid = intent.getIntExtra("friendid", 0);
		if (friendid == 0) {
			int userid = HP_User.getOnLineUserId(this);
			if (userid != 0) {
				HP_User user = HP_DBModel.getInstance(this)
						.queryUserInfoByUserId(HP_User.getOnLineUserId(this),
								true);
				textViewMyName.setText(user.userNick);
				age_txt.setText( UserInfoUtil.getAgeFromBirthDay(user.userAge )+ "岁");
				if (user.userHeight != 0 && user.userWeight != 0) {
					weight_txt.setText(UserIndexUtils.getResult(this, user));
					weight_txt.setVisibility(View.VISIBLE);
				} else {
					weight_txt.setVisibility(View.GONE);
				}
				if (user.userSex == -1) {
					sex_txt.setImageResource(R.drawable.boy_icon);
				} else {
					sex_txt.setImageResource(R.drawable.girl_icon);
				}
				ImageLoader.getInstance(this).addTask(user.photourl,
						imageViewMyDetail);
			}
			new GetMyMomentsTask().execute();
		} else {

			String friendName = intent.getStringExtra("friendname");
			textViewMyName.setText(friendName);
			String friendAvatar = intent.getStringExtra("friendavatar");
			int friendsex = intent.getIntExtra("friendsex", -1);
			if (friendsex == -1) {
				sex_txt.setImageResource(R.drawable.boy_icon);
			} else {
				sex_txt.setImageResource(R.drawable.girl_icon);
			}
			int friendage = intent.getIntExtra("friendage", 0);
			age_txt.setText(UserInfoUtil.getAgeFromBirthDay(friendage)+ "岁");
			System.out.println("年龄"+friendage);
			Float friendweight = intent.getFloatExtra("friendweight", 0);
			int friendheight = intent.getIntExtra("friendheight", 0);
			if (friendweight != 0 && friendheight != 0) {
				HP_User user = new HP_User();
				user.userWeight = friendweight;
				user.userHeight = friendheight;
				user.userSex = friendsex;
				weight_txt.setText(UserIndexUtils.getResult(this, user));
				weight_txt.setVisibility(View.VISIBLE);
			} else {
				weight_txt.setVisibility(View.GONE);
			}

			ImageLoader.getInstance(this).addTask(friendAvatar,
					imageViewMyDetail);
			new GetFriendMomentsTask().execute(friendid);
		}

		settitle();
	}

	void settitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_lbtn_img.setImageResource(R.drawable.hp_w_header_view_back);
		// header_rbtn_img.setImageResource(R.drawable.moments_add_selector);
		header_text.setText(getString(R.string.mymoments));
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		header_rbtn_img.setOnClickListener(this);
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetMyMomentsTask extends AsyncTask<Void, Void, List<Moments>> {

		private MultiValueMap<String, String> formData;
		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(UserMomentsActivity.this);
			dialog.show();
		}

		@Override
		protected List<Moments> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				int myaccountid = SpringAndroidService.getInstance(
						getApplication()).getMyId();
				List<Moments> myMomentsList = SpringAndroidService.getInstance(
						getApplication()).getMomentsListByAccountid(
						myaccountid, 2000000000, 200);
				
				return myMomentsList;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Moments> myMomentsList) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {

			}

			if (myMomentsList != null) {
				System.out
						.println("myMomentsList size:" + myMomentsList.size());
				momentsAdapter.addMomentsList(myMomentsList);
				momentsAdapter.notifyDataSetChanged();
			}
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetFriendMomentsTask extends
			AsyncTask<Integer, Void, List<Moments>> {

		private MultiValueMap<String, String> formData;
		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new ProgressDialog(UserMomentsActivity.this);
			dialog.show();
		}

		@Override
		protected List<Moments> doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int friendid = params[0];
			try {
				List<Moments> myMomentsList = SpringAndroidService.getInstance(
						getApplication()).getMomentsListByAccountid(friendid,
						2000000000, 200);

				return myMomentsList;

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<Moments> myMomentsList) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {

			}

			if (myMomentsList != null) {
				momentsAdapter.addMomentsList(myMomentsList);
				momentsAdapter.notifyDataSetChanged();
			}
		}
	}

	private class MomentsAdapter extends BaseAdapter {
		Context context;
		LayoutInflater inflater;
		List<Moments> momentsList;
		private Object lock = new Object();

		public MomentsAdapter(Context context) {
			super();
			this.context = context;
			inflater = LayoutInflater.from(context);
			momentsList = new ArrayList<Moments>();
		}

		public void addMomentsList(List<Moments> addMomentsList) {
			momentsList.clear();
			momentsList.addAll(addMomentsList);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return momentsList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			if (convertView != null) {
				view = convertView;
			} else {
				view = (View) inflater.inflate(R.layout.mymoments_list_item,
						parent, false);
			}
			TextView textViewMessage = (TextView) view
					.findViewById(R.id.tv_content);
			ImageView ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
			Moments moments = momentsList.get(position);
			Timestamp ts = moments.getCreatetime();
			SimpleDateFormat formattimeym = new SimpleDateFormat("yyyy.MM");
			Date date = new Date(ts.getTime());
			String formatdateym = formattimeym.format(date);
			SimpleDateFormat formattimed = new SimpleDateFormat("dd");
			String formatdated = formattimed.format(date);

			TextView textViewYM = (TextView) view.findViewById(R.id.tv_timeym);
			TextView textViewD = (TextView) view.findViewById(R.id.tv_timed);

			textViewYM.setText(formatdateym);
			textViewD.setText(formatdated);

			String strImage1 = moments.getImage1();
			if (strImage1 == null || strImage1.length() == 0) {
				ivPhoto.setVisibility(View.GONE);
			} else {
				ivPhoto.setVisibility(View.VISIBLE);
				// ivPhoto.setImageResource(R.drawable.myhealth_users_avatar);
			}
			textViewMessage.setText(moments.getMessage());
			if (strImage1 != null && strImage1.length() > 0) {
				ImageLoader.getInstance(UserMomentsActivity.this).addTask(
						moments.getImage1(), ivPhoto);
			}

			/*
			 * ImageViewGet imageViewGet = new ImageViewGet();
			 * imageViewGet.setImageurl(moments.getImage1());
			 * imageViewGet.setImageViewMoments(ivPhoto);
			 * 
			 * new GetImageTask().execute(imageViewGet);
			 */
			/*
			 * TextView textViewMessage = (TextView) view
			 * .findViewById(R.id.momentsmessage); ImageView imageViewMoments =
			 * (ImageView) view .findViewById(R.id.momentsimage); Moments
			 * moments = momentsList.get(position);
			 * textViewMessage.setText(moments.getMessage());
			 * 
			 * ImageViewGet imageViewGet = new ImageViewGet();
			 * imageViewGet.setImageurl(moments.getImage1());
			 * imageViewGet.setImageViewMoments(imageViewMoments);
			 * 
			 * new GetImageTask().execute(imageViewGet);
			 */
			return view;
		}
	}

	
	private class ImageViewGet implements Serializable {
		private ImageView imageViewMoments;
		private String imageurl;

		public ImageView getImageViewMoments() {
			return imageViewMoments;
		}

		public void setImageViewMoments(ImageView imageViewMoments) {
			this.imageViewMoments = imageViewMoments;
		}

		public String getImageurl() {
			return imageurl;
		}

		public void setImageurl(String imageurl) {
			this.imageurl = imageurl;
		}
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class GetImageTask extends AsyncTask<ImageViewGet, Void, byte[]> {

		private MultiValueMap<String, String> formData;
		private Exception exception;
		ProgressDialog dialog;
		ImageViewGet imageViewGet;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			imageViewGet = null;
		}

		@Override
		protected byte[] doInBackground(ImageViewGet... params) {
			// TODO Auto-generated method stub
			imageViewGet = params[0];
			String imageUrl = imageViewGet.getImageurl();
			// String imageUrl =
			// "http://ww2.sinaimg.cn/bmiddle/66a36aa8jw1efbpnrdo04j20hs0hsmyj.jpg";
			try {
				byte[] response = SpringAndroidService.getInstance(
						getApplication()).downloadImageByUrl(imageUrl);
				return response;
			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(byte[] image) {
			// TODO Auto-generated method stub
			if (exception != null) {
				System.out.println("-------------");
				System.out.println(exception.getMessage());
			} else {
				System.out.println("------image size-------" + image.length);
				Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,
						image.length);
				imageViewGet.getImageViewMoments().setImageBitmap(bitmap);
			}
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.header_lbtn_img:
			this.finish();
			break;
		case R.id.header_rbtn_img:
			break;

		default:
			break;
		}
	}
}
