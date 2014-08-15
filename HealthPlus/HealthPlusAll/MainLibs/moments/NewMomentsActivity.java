package com.vee.moments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executors;

import org.springframework.util.MultiValueMap;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_new_moments, null);
		setContainer(view);
		// setContentView(R.layout.activity_new_moments);
		Intent intent = getIntent();
		bitmap1path = intent.getStringExtra("bitmap");
		System.out.println("bitmap1path --- " + bitmap1path);
		getHeaderView().setHeaderTitle("");
		getHeaderView().setBackGroundColor(R.color.blue);
		setRightBtnVisible(View.VISIBLE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		getHeaderView().setRightOption(HeaderView.HEADER_CAMERA);

		setHeaderClickListener(new OnHeaderClickListener() {

			@Override
			public void OnHeaderClick(View v, int option) {
				// TODO Auto-generated method stub
				if (option == HeaderView.HEADER_OK) {
					if (option == HeaderView.HEADER_CAMERA) {
						Toast.makeText(NewMomentsActivity.this, "setting",
								Toast.LENGTH_SHORT).show();
						if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
							new UploadMomentsPhotoTask()
									.executeOnExecutor(Executors
											.newCachedThreadPool());
						} else {
							new UploadMomentsPhotoTask().execute();
						}
					} else if (option == HeaderView.HEADER_BACK) {
						NewMomentsActivity.this.finish();
					}
				}
			}
		});

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			new CompressPhotoTask().executeOnExecutor(Executors
					.newCachedThreadPool());
		} else {
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
			EditText textMessage = (EditText) findViewById(R.id.editTextThisMoment);
			message = textMessage.getText().toString();

			dialog = new ProgressDialog(NewMomentsActivity.this);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				UploadPhotoResponse uploadPhotoResponse = SpringAndroidService
						.getInstance(getApplication()).uploadMomentsPhoto(
								bitmap1smallpath);
				SpringAndroidService.getInstance(getApplication())
						.insertMoments(message,
								uploadPhotoResponse.getPhotourl(), null, null,
								null, null, null, null, null, null);

			} catch (Exception e) {
				this.exception = e;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
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
			File file = new File(bitmap1smallpath);
			try {
				file.createNewFile();
				OutputStream out = new FileOutputStream(file);
				// 声明写入的格式 ，质量 ，写入到哪里
				System.out.println("bm+" + bm);
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
