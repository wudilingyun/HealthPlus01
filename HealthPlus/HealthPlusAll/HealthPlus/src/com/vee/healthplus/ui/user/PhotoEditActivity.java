package com.vee.healthplus.ui.user;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
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
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;

public class PhotoEditActivity extends Activity implements View.OnClickListener {
	private Button cancelBtn, takeBtn, pickBtn;
	private HP_User user;
	private ICallBack callBack;
	private static final int PHOTO_PICKED_WITH_DATA = 1020;
	private static final int CAMERA_WITH_DATA = 1021;
	private static final int PHOTO_CROP = 1022;
	private int userId;
	private String hdFileName;
	private Uri u;
	private Uri tempUri;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case CAMERA_WITH_DATA:
			if (resultCode == RESULT_OK) {
				doCropPhoto2(tempUri);
			}
			break;
		case PHOTO_PICKED_WITH_DATA:
			if (resultCode == RESULT_OK) {
				doCropPhoto2(data.getData());
			}
			break;
		case PHOTO_CROP:
			if (resultCode == RESULT_OK) {
				Intent result = new Intent();
				result.putExtra("hd", u.toString());
				setResult(RESULT_OK, result);
			} else {

				File hf = new File(Environment.getExternalStorageDirectory(),
						hdFileName);
				if (hf.exists()) {
					hf.delete();
				}
				setResult(RESULT_CANCELED);

			}
			File temp = new File(Environment.getExternalStorageDirectory(),
					"hp_temp_head.jpg");
			if (temp.exists()) {
				temp.delete();
			}
			finish();
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
		setContentView(R.layout.personal_info_photo_edit_layout);
		cancelBtn = (Button) findViewById(R.id.photo_edit_cancel_btn);
		takeBtn = (Button) findViewById(R.id.photo_edit_take_btn);
		pickBtn = (Button) findViewById(R.id.photo_edit_pick_btn);
		cancelBtn.setOnClickListener(this);
		takeBtn.setOnClickListener(this);
		pickBtn.setOnClickListener(this);
		userId = getIntent().getIntExtra("id", -1);
		hdFileName = "photo_temp" + "/" + "hd" + userId + ".jpg";
		File hf = new File(Environment.getExternalStorageDirectory(),
				hdFileName);
		File temp = new File(Environment.getExternalStorageDirectory(),
				"hp_temp_head.jpg");
		if (!hf.exists()) {
			makeDir(hf.getParentFile());
		}
		if (temp.exists()) {
			temp.delete();
		}
		try {
			temp.createNewFile();
			hf.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		u = Uri.fromFile(hf);
		tempUri = Uri.fromFile(temp);
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
			Log.i("lingyun", "ugetPath=" + tempUri.getPath());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
			startActivityForResult(intent, CAMERA_WITH_DATA);
			break;
		case R.id.photo_edit_pick_btn:
			Intent innerIntent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			// Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
			// innerIntent.addCategory(Intent.CATEGORY_OPENABLE);
			// innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
			innerIntent.setType("image/*");
			Intent wrapperIntent = Intent.createChooser(innerIntent, null);
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

	private void doCropPhoto2(Uri uri) {
		Log.i("lingyun", "doCropPhoto2");
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, this.u);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, PHOTO_CROP);
	}

}
