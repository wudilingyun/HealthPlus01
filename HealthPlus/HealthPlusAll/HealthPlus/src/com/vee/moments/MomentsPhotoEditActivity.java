package com.vee.moments;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;

/**
 * Created by zhou on 13-10-28.
 */
public class MomentsPhotoEditActivity extends Activity implements
		View.OnClickListener {
	private Button cancelBtn, takeBtn, pickBtn, textBtn;
	private HP_User user;
	private ICallBack callBack;
	private static final int PHOTO_PICKED_WITH_DATA = 1020;
	private static final int CAMERA_WITH_DATA = 1021;

	private static final int PHOTO_CROP = 1022;

	private static final int SELECT_PIC_KITKAT = 1025;
	private static final int SELECT_PIC = 1026;
	private int userId;
	private String hdFileName;
	private Uri u;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case 10: {
			setResult(resultCode);
			finish();
			break;
		}
		case CAMERA_WITH_DATA:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(MomentsPhotoEditActivity.this,
						NewMomentsActivity.class);
				intent.putExtra("bitmap", u.getPath());
				startActivityForResult(intent, 10);
				/*
				 * Intent intent = getIntent(); intent.putExtra("photopath",
				 * u.getPath());
				 */
			} else {
				finish();
			}
			// finish();
			break;
		case PHOTO_PICKED_WITH_DATA:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(MomentsPhotoEditActivity.this,
						NewMomentsActivity.class);
				Uri uri = data.getData();
				Cursor cursor = getContentResolver().query(uri, null, null,
						null, null);
				cursor.moveToFirst();
				String imgNo = cursor.getString(0); // 图片编号
				String imgPath = cursor.getString(1); // 图片文件路径
				String imgSize = cursor.getString(2); // 图片大小
				String imgName = cursor.getString(3); // 图片文件名
				intent.putExtra("bitmap", imgPath);
				startActivityForResult(intent, 10);
				/*
				 * Intent intent = getIntent(); intent.putExtra("photopath",
				 * imgPath);
				 */
			} else {
				finish();
			}

			// finish();
			break;
		case PHOTO_CROP:
			if (resultCode == RESULT_OK) {
				Intent result = new Intent();
				result.putExtra("hd", u.toString());
				setResult(RESULT_OK, result);
				finish();
			} else {
				File temp = new File(Environment.getExternalStorageDirectory(),
						hdFileName);
				if (temp.exists()) {
					temp.delete();
				}
				setResult(RESULT_CANCELED);
				finish();
			}
			break;
		}
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window window = getWindow();
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.dialog_updown);
		setContentView(R.layout.moments_photo_edit_layout);
		cancelBtn = (Button) findViewById(R.id.photo_edit_cancel_btn);
		takeBtn = (Button) findViewById(R.id.photo_edit_take_btn);
		pickBtn = (Button) findViewById(R.id.photo_edit_pick_btn);
		textBtn = (Button) findViewById(R.id.photo_edit_text_btn);
		cancelBtn.setOnClickListener(this);
		takeBtn.setOnClickListener(this);
		pickBtn.setOnClickListener(this);
		textBtn.setOnClickListener(this);
		userId = getIntent().getIntExtra("id", -1);
		hdFileName = "photo_temp" + "/" + "hd" + userId + ".jpg";
		File temp = new File(Environment.getExternalStorageDirectory(),
				hdFileName);
		if (!temp.exists()) {
			makeDir(temp.getParentFile());
		}
		try {
			temp.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		u = Uri.fromFile(temp);
		u = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				"photograph.jpg"));
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.photo_edit_cancel_btn:
			finish();
			break;
		case R.id.photo_edit_take_btn:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Log.i("lingyun", "ugetPath=" + u.getPath());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(intent, CAMERA_WITH_DATA);
			break;
		case R.id.photo_edit_text_btn:
			Intent intentText = new Intent(MomentsPhotoEditActivity.this,
					NewMomentsActivity.class);
			intentText.putExtra("text", "on");
			startActivityForResult(intentText, 10);

			break;
		case R.id.photo_edit_pick_btn:
			Intent innerIntent;
			/*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
				innerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			} else {
				innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
			}*/
			
			
			innerIntent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			//innerIntent.addCategory(Intent.CATEGORY_OPENABLE);
			innerIntent.setType("image/*");
			Intent wrapperIntent = Intent.createChooser(innerIntent, null);
			// startActivityForResult(wrapperIntent, PHOTO_PICKED_WITH_DATA);
			/*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
				startActivityForResult(wrapperIntent, CAMERA_WITH_DATA);
			} else {
				startActivityForResult(wrapperIntent, PHOTO_PICKED_WITH_DATA);
			}*/
			
			startActivityForResult(wrapperIntent, PHOTO_PICKED_WITH_DATA);
			break;
		}
	}

	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	public void doCropPhoto2(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 128);
		intent.putExtra("outputY", 128);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, this.u);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, PHOTO_CROP);
	}

}
