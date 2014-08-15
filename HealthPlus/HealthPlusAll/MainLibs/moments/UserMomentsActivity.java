package com.vee.moments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.util.MultiValueMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.yunfox.s4aservicetest.response.Moments;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class UserMomentsActivity extends BaseFragmentActivity {

	private MomentsAdapter momentsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_my_moments, null);
		setContainer(view);

		ImageView imageViewMyDetail = (ImageView) findViewById(R.id.mydetail);
		ListView listViewMomentsList = (ListView) findViewById(R.id.momentslist);

		momentsAdapter = new MomentsAdapter(UserMomentsActivity.this);
		listViewMomentsList.setAdapter(momentsAdapter);

		Intent intent = getIntent();
		int friendid = intent.getIntExtra("friendid", 0);
		if (friendid == 0) {
			if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
			{
				new GetMyMomentsTask().executeOnExecutor(Executors.newCachedThreadPool());
			}
			else
			{
				new GetMyMomentsTask().execute();
			}
		} else {
			if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
			{
				new GetFriendMomentsTask().executeOnExecutor(Executors.newCachedThreadPool());
			}
			else
			{
				new GetFriendMomentsTask().execute();
			}
		}
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
						getApplication()).getMomentsListByAccountid(myaccountid, 2000000000, 20);

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
				System.out.println("myMomentsList size:" + myMomentsList.size());
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
						2000000000, 20);

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
			System.out.println("---getView invoked");
			View view = null;
			if (convertView != null) {
				view = convertView;
			} else {
				view = (View) inflater.inflate(R.layout.moments_list_item,
						parent, false);
			}
			TextView textViewMessage = (TextView) view
					.findViewById(R.id.momentsmessage);
			ImageView imageViewMoments = (ImageView) view
					.findViewById(R.id.momentsimage);
			Moments moments = momentsList.get(position);
			textViewMessage.setText(moments.getMessage());

			ImageViewGet imageViewGet = new ImageViewGet();
			imageViewGet.setImageurl(moments.getImage1());
			imageViewGet.setImageViewMoments(imageViewMoments);
			if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
			{
				new GetImageTask().executeOnExecutor(Executors.newCachedThreadPool(),imageViewGet);
			}
			else
			{
				new GetImageTask().execute(imageViewGet);
			}
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
			//String imageUrl = "http://ww2.sinaimg.cn/bmiddle/66a36aa8jw1efbpnrdo04j20hs0hsmyj.jpg";
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
}
