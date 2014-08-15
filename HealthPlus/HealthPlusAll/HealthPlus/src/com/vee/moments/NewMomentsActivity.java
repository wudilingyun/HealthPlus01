package com.vee.moments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.util.MultiValueMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.widget.HeaderView;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;
import com.yunfox.s4aservicetest.response.UploadPhotoResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class NewMomentsActivity extends BaseFragmentActivity {
	
	private byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	String bitmap1path = null;
	String bitmap1smallpath = null;
	private 	EditText textMessage;
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View view = null;
		Intent intent = getIntent();
		String text = intent.getStringExtra("text");
		if (text != null && text.length() > 0) {
			view = View.inflate(this, R.layout.moments_activity_publishtext,
					null);
			getHeaderView().setHeaderTitle("发表文字");
		} else {
			view = View.inflate(this, R.layout.activity_new_moments, null);
			getHeaderView().setHeaderTitle("发表图片");
		}

		setContainer(view);
		// setContentView(R.layout.activity_new_moments);

		
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		setLeftBtnRes(R.drawable.hp_w_header_view_back);

		setHeaderClickListener(new OnHeaderClickListener() {

			@Override
			public void OnHeaderClick(View v, int option) {
				// TODO Auto-generated method stub
				if (option == HeaderView.HEADER_BACK) {
					NewMomentsActivity.this.finish();
				}
			}
		});
		 textMessage = (EditText) findViewById(R.id.editTextThisMoment);
		Button saveMomentsButton = (Button) findViewById(R.id.SaveMomentsButton);
		saveMomentsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(textMessage!=null&&textMessage.length()>0){
					new UploadMomentsPhotoTask().execute();
				}else {
					Toast.makeText(getApplication(), "内容不能为空", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		if (text != null && text.length() > 0) {
			bitmap1path = null;
			/*ImageView imageViewFirst = (ImageView) findViewById(R.id.imageViewFirst);
			imageViewFirst.setVisibility(View.GONE);*/
		} else {
			bitmap1path = intent.getStringExtra("bitmap");
			new CompressPhotoTask().execute();
			
		}
		
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}

	// ***************************************
	// Private classes
	// ***************************************
	private class UploadMomentsPhotoTask extends AsyncTask<Void, Void, Void> {

		private MultiValueMap<String, String> formData;
		private Exception exception;
		ProgressDialog dialog;
		private String message;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			message = textMessage.getText().toString();
			System.out.println("message"+message);
			dialog = new ProgressDialog(NewMomentsActivity.this);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				if (bitmap1smallpath != null) {
					UploadPhotoResponse uploadPhotoResponse = SpringAndroidService
							.getInstance(getApplication()).uploadMomentsPhoto(
									bitmap1smallpath);
					SpringAndroidService.getInstance(getApplication())
							.insertMoments(message,
									uploadPhotoResponse.getPhotourl(), null,
									null, null, null, null, null, null, null);
				} else {
					SpringAndroidService.getInstance(getApplication())
							.insertMoments(message, null, null, null, null,
									null, null, null, null, null);
					System.out.println("发送文字");
					
				}
			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if (exception != null) {
				System.out.println(exception.getMessage());
				Toast.makeText(NewMomentsActivity.this, "保存失败，请重试",
						Toast.LENGTH_LONG).show();
			} else {
				setResult(RESULT_OK);
				NewMomentsActivity.this.finish();
			}
		}
	}

	private class CompressPhotoTask extends AsyncTask<Void, Void, Bitmap> {
		private Exception exception;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(NewMomentsActivity.this);
			dialog.show();
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Bitmap bm = getSmallBitmap(bitmap1path);
			int degree = readPictureDegree(bitmap1path);
			if (degree != 0) {
				bm = rotateBitmap(bm, degree);
			}

			bitmap1smallpath = bitmap1path.replace(".jpg", "_small.jpg");
			// File file = new File(bitmap1smallpath);
			File file = new File(Environment.getExternalStorageDirectory(),
					"photograph_small.jpg");
			bitmap1smallpath = file.getAbsolutePath();
			try {
				file.createNewFile();
				OutputStream out = new FileOutputStream(file);
				// 声明写入的格式 ，质量 ，写入到哪里
				System.out
						.println("bm+" + bm + "file" + file.getAbsolutePath());
				bm.compress(Bitmap.CompressFormat.JPEG, 30, out);
				out.flush();
				out.close();

				return bm;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.exception = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bm) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			ImageView imageViewFirst = (ImageView) findViewById(R.id.imageViewFirst);
			imageViewFirst.setImageBitmap(bm);
		}
	}
}
