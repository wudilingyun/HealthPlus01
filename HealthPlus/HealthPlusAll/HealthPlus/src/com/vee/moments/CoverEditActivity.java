package com.vee.moments;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.vee.healthplus.R;

public class CoverEditActivity extends Activity implements View.OnClickListener {
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case CAMERA_WITH_DATA:
			if (resultCode == RESULT_OK) {
				doCropPhoto2(u);
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
				result.putExtra("cover", u.toString());
				result.putExtra("coverpath", u.getPath());
				setResult(RESULT_OK, result);
				finish();
			} else {
				File temp = new File(Environment.getExternalStorageDirectory(),
						coverFileName);
				if (temp.exists()) {
					temp.delete();
				}
				setResult(RESULT_CANCELED);
				finish();
			}
			break;
		}
	}
	
	public void doCropPhoto2(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, this.u);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, PHOTO_CROP);
	}

	private static final int PHOTO_PICKED_WITH_DATA = 1020;
	private static final int CAMERA_WITH_DATA = 1021;
	private static final int PHOTO_CROP = 1022;
	private Button cameraoneCoverButton;
	private Button albumCoverButton;
	private String coverFileName;
	private Uri u;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.changecover));;
		setContentView(R.layout.activity_cover_edit);
		
		cameraoneCoverButton = (Button)findViewById(R.id.cameraonecover_btn);
		albumCoverButton = (Button)findViewById(R.id.albumcover_btn);
		
		cameraoneCoverButton.setOnClickListener(this);
		albumCoverButton.setOnClickListener(this);
		
		coverFileName = "photo_temp" + "/" + "cover.jpg";
		File temp = new File(Environment.getExternalStorageDirectory(),
				coverFileName);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cover_edit, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()){
		case R.id.cameraonecover_btn:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Log.i("lingyun", "ugetPath=" + u.getPath());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(intent, CAMERA_WITH_DATA);
			break;
		case R.id.albumcover_btn:
			Intent innerIntent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
}
