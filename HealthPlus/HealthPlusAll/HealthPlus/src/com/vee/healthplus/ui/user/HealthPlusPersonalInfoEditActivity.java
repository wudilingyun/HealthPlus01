package com.vee.healthplus.ui.user;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.springframework.social.greenhouse.api.Profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.heahth_news_utils.ImageFileCache;
import com.vee.healthplus.heahth_news_utils.ImageMemoryCache;
import com.vee.healthplus.util.AppPreferencesUtil;
import com.vee.healthplus.util.user.GetProfileTask;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.util.user.SaveProfileTask;
import com.vee.healthplus.util.user.UserInfoUtil;
import com.vee.healthplus.widget.CustomProgressDialog;

@SuppressLint("ResourceAsColor")
public class HealthPlusPersonalInfoEditActivity extends Activity implements
		View.OnClickListener, ICallBack, SaveProfileTask.SaveProfileCallBack,
		GetProfileTask.GetProfileCallBack {

	private ListView mListView;
	private Button saveBtn;
	private ArrayList<ListElement> infoList;
	private PersonalInfoAdapter mAdapter;
	private HP_User user;
	private Bitmap head = null;
	private int mAge;
	private ImageFileCache fileCache;
	private ImageMemoryCache memoryCache;
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;
	private ImageLoader imageLoader;
	private String hdpath;
	private ProgressDialog dialog;
	private int offset;

	private CustomProgressDialog progressDialog = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.i("lingyun", "hppie.onActivityResult,requestCode=" + requestCode
				+ ",resultCode=" + resultCode);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 0:
				String uri = data.getStringExtra("hd");
				hdpath = Uri.parse(uri).getPath();
				Log.i("lingyun", "uri=" + uri);
				try {
					head = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), Uri.parse(uri));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				infoList.get(0).setPhoto(head);
				break;
			case 1:
				Bundle b1 = data.getExtras();
				String uname = b1.getString("uname");
				if (uname != null && !uname.equals("")) {
					infoList.get(1).setValue(uname);
				}
				break;
			case 2:
				break;
			case 3:
				Bundle b3 = data.getExtras();
				String sex = b3.getString("sex");
				if (sex != null && !sex.equals("")) {
					infoList.get(3 - offset).setValue(sex);
				}
				break;
			case 4:
				Bundle b4 = data.getExtras();
				mAge = b4.getInt("age");
				infoList.get(4 - offset).setValue(
						UserInfoUtil.getAgeFromBirthDay(mAge) + "岁");
				break;
			case 5:
				Bundle b5 = data.getExtras();
				String email = b5.getString("email");
				if (email != null && !email.equals("")) {
					infoList.get(5 - offset).setValue(email);
				}
				break;
			case 6:
				Bundle b6 = data.getExtras();
				String height = b6.getString("height");
				if (height != null && !height.equals("")) {
					infoList.get(6 - offset).setValue(height + "cm");
				}
				break;
			case 7:
				Bundle b7 = data.getExtras();
				String weight = b7.getString("weight");
				if (weight != null && !weight.equals("")) {
					infoList.get(7 - offset).setValue(weight + "kg");
				}
				break;

			}
		}

		mAdapter.notifyDataSetChanged();
		super.onActivityResult(requestCode, resultCode, data);
	}

	public HealthPlusPersonalInfoEditActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_info_edit_layout);

		user = HP_DBModel.getInstance(this).queryUserInfoByUserId(
				HP_User.getOnLineUserId(this), true);
		imageLoader = ImageLoader.getInstance(this);
		dialog = new ProgressDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		initView();
		initData();
	}

	private void initView() {
		View headView = findViewById(R.id.personal_info_headview);
		header_text = (TextView) headView.findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) headView
				.findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) headView
				.findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText("个人信息");
		header_lbtn_img
				.setImageResource(R.drawable.healthplus_headview_back_btn);
		mListView = (ListView) findViewById(R.id.health_plus_personal_info_edit_lv);
		saveBtn = (Button) findViewById(R.id.health_plus_personal_info_edit_sava_btn);
		saveBtn.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
		// String digits =
		// getResources().getString(R.string.user_resgiter_edit);
		// userPwd_et.setKeyListener(DigitsKeyListener.getInstance(digits));
		// userName_et.setKeyListener(DigitsKeyListener.getInstance(digits));
	}

	private void initData() {
		memoryCache = new ImageMemoryCache(this);
		fileCache = new ImageFileCache();
		infoList = new ArrayList<ListElement>();
		mAdapter = new PersonalInfoAdapter(this);
		ImageListViewItem imageItem = new ImageListViewItem(user, imageLoader);
		infoList.add(imageItem);
		for (int i = 0; i < 7; i++) {
			infoList.add(new TextListViewItem());
		}

		infoList.get(0).setText("头像").setTag(0);
		infoList.get(1).setText("昵称").setValue(user.userNick).setTag(1);
		infoList.get(2).setText("密码").setValue("点击修改密码").setTag(2);
		infoList.get(3).setText("性别").setValue(user.userSex == -1 ? "男" : "女")
				.setTag(3);
		infoList.get(4).setText("年龄")
				.setValue(UserInfoUtil.getAgeFromBirthDay(user.userAge) + "岁")
				.setTag(4);
		infoList.get(5).setText("邮箱").setValue(user.email).setTag(5);
		infoList.get(6).setText("身高").setValue(user.userHeight + "cm")
				.setTag(6);
		DecimalFormat format2 = new DecimalFormat("0");
		infoList.get(7)
				.setText("体重")
				.setValue(
						Float.parseFloat(format2.format(user.userWeight))
								+ "kg").setTag(7);
		Log.i("lingyun",
				"AppPreferencesUtil.getBooleanPref(context,isQQLogin, false)="
						+ AppPreferencesUtil.getBooleanPref(this, "isQQLogin",
								false));
		if (AppPreferencesUtil.getBooleanPref(this, "isQQLogin", false)) {
			infoList.remove(2);
			offset = 1;
		}
		mAdapter.setList(infoList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch ((Integer) view.getTag()) {
				case 0:
					Bundle extras0 = new Bundle();
					extras0.putInt("id", user.userId);
					Intent intent0 = new Intent();
					intent0.putExtras(extras0);
					intent0.setClass(HealthPlusPersonalInfoEditActivity.this,
							PhotoEditActivity.class);
					startActivityForResult(intent0, 0);
					break;
				case 1:
					Bundle extras1 = new Bundle();
					Intent intent1 = new Intent();
					extras1.putString("uname", infoList.get(1).getValue());
					intent1.putExtras(extras1);
					intent1.setClass(HealthPlusPersonalInfoEditActivity.this,
							UsernameEditActivity.class);
					startActivityForResult(intent1, 1);
					break;
				case 2:
					startActivity(new Intent(
							HealthPlusPersonalInfoEditActivity.this,
							HealthPlusModifyPwdActivity.class));
					break;
				case 3:
					Bundle extras3 = new Bundle();
					Intent intent3 = new Intent();
					extras3.putString("sex", infoList.get(3 - offset)
							.getValue());
					intent3.putExtras(extras3);
					intent3.setClass(HealthPlusPersonalInfoEditActivity.this,
							SexEditActivity.class);
					startActivityForResult(intent3, 3);
					break;
				case 4:
					Bundle extras4 = new Bundle();
					Intent intent4 = new Intent();
					extras4.putInt("age", user.userAge);
					intent4.putExtras(extras4);
					intent4.setClass(HealthPlusPersonalInfoEditActivity.this,
							AgeEditActivity.class);
					startActivityForResult(intent4, 4);
					break;
				case 5:
					Bundle extras5 = new Bundle();
					Intent intent5 = new Intent();
					extras5.putString("email", infoList.get(5 - offset)
							.getValue());
					intent5.putExtras(extras5);
					intent5.setClass(HealthPlusPersonalInfoEditActivity.this,
							EmailEditActivity.class);
					startActivityForResult(intent5, 5);
					break;
				case 6:
					Bundle extras6 = new Bundle();
					Intent intent6 = new Intent();
					extras6.putString("height", infoList.get(6 - offset)
							.getValue());
					intent6.putExtras(extras6);
					intent6.setClass(HealthPlusPersonalInfoEditActivity.this,
							HeightEditActivity.class);
					startActivityForResult(intent6, 6);
					break;
				case 7:
					Bundle extras7 = new Bundle();
					Intent intent7 = new Intent();
					extras7.putString("weight", infoList.get(7 - offset)
							.getValue());
					intent7.putExtras(extras7);
					intent7.setClass(HealthPlusPersonalInfoEditActivity.this,
							WeightEditActivity.class);
					startActivityForResult(intent7, 7);
					break;
				case 8:
					break;

				}

			}
		});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.health_plus_personal_info_edit_sava_btn:
			if (!CheckNetWorkStatus.Status(this)) {
				Toast.makeText(this, "请检查网络连接", Toast.LENGTH_SHORT).show();
				return;
			}
			user.userNick = infoList.get(1).getValue();
			user.email = infoList.get(5 - offset).getValue();
			String str = infoList.get(6 - offset).getValue();
			user.userHeight = Integer
					.valueOf(str.substring(0, str.length() - 2));
			str = infoList.get(7 - offset).getValue();
			user.userWeight = Float.valueOf(str.substring(0, str.length() - 2));
			str = infoList.get(4 - offset).getValue();
			user.userAge = mAge;
			str = infoList.get(3 - offset).getValue();
			user.userSex = str.equals("男") ? -1 : 0;
			if (HP_User.getOnLineUserId(this) != 0) {
				try {
					new SaveProfileTask(
							HealthPlusPersonalInfoEditActivity.this, user,
							HealthPlusPersonalInfoEditActivity.this, hdpath)
							.execute();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			HP_DBModel.getInstance(this).updateUserInfo(user, true);
			dialog.show();
			break;
		case R.id.header_lbtn_img:
			onBackPressed();
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage(this.getString(R.string.registing));
		progressDialog.setCanceledOnTouchOutside(false);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinishSaveProfile(int reflag) {
		// TODO Auto-generated method stub
		System.out.print("onFinishSaveProfile.reflag=" + reflag);
	}

	@Override
	public void onErrorSaveProfile(Exception e) {
		// TODO Auto-generated method stub
		dialog.dismiss();
		finishSelf();
	}

	@Override
	public void onFinishUploadAvatar(int reflag) {
		// TODO Auto-generated method stub
		System.out.print("onFinishUploadAvatar.reflag=" + reflag);
		if (reflag == 200) {
			new GetProfileTask(this, this).execute();
		} else {
			dialog.dismiss();
			Toast.makeText(this, "头像保存失败", Toast.LENGTH_SHORT).show();
			finishSelf();
		}
	}

	@Override
	public void onFinishGetProfile(Profile profile) {
		// TODO Auto-generated method stub
		user.photourl = profile.getRawavatarurl();
		HP_DBModel.getInstance(this).updateUserInfo(user, true);
		if (head != null) {
			fileCache.saveBitmap(head, user.photourl);
			memoryCache.addBitmapToCache(user.photourl, head);
		}

		dialog.dismiss();
		finishSelf();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finishSelf();
	}

	@Override
	public void onErrorGetProfile(Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onErrorUploadAvatar() {
		finishSelf();
	}

	private void finishSelf() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			Intent i = new Intent();
			i.setComponent((ComponentName) b.getParcelable("cn"));
			Log.i("lingyun", "start infoedit ComponentName="
					+ ((ComponentName) b.getParcelable("cn")).toString());
			startActivity(i);
		} else {
			Log.i("lingyun", "start infoedit ComponentName=null");
		}
		finish();
	}

}
