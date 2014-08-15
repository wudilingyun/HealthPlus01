package com.vee.healthplus.ui.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.user.UpdatePwdTask;
import com.vee.healthplus.widget.CustomProgressDialog;
import com.vee.healthplus.widget.HeaderView;

public class HealthPlusModifyPwdActivity extends BaseFragmentActivity implements
		UpdatePwdTask.UpdatePwdCallBack, View.OnClickListener ,OnFocusChangeListener{

	private EditText oldPwd_et, newPwd_et, confirm_et;
	private CustomProgressDialog progressDialog;
	private ImageView old_img,new_img,confirm_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.healthplus_modify_pwd_layout,
				null);
		setContainer(view);
		getHeaderView().setHeaderTitle("修改密码");
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(HeaderView.HEADER_BACK);
		initView(view);
	}

	@Override
	public void onResume() {
		super.onResume();
		progressDialog = CustomProgressDialog.createDialog(this);
		progressDialog.setMessage(this.getString(R.string.changing_pwd));
	}

	private void initView(View view) {
		oldPwd_et = (EditText) view
				.findViewById(R.id.health_plus_modify_pwd_old_et);
		newPwd_et = (EditText) view
				.findViewById(R.id.health_plus_modify_pwd_new_et);
		confirm_et = (EditText) view
				.findViewById(R.id.health_plus_modify_pwd_confirm_et);
		Button motify_btn = (Button) view
				.findViewById(R.id.health_plus_modify_pwd_submit_btn);
		old_img=(ImageView)view.findViewById(R.id.health_plus_modify_pwd_old_iv);
		new_img=(ImageView)view.findViewById(R.id.health_plus_modify_pwd_new_iv);
		confirm_img=(ImageView)view.findViewById(R.id.health_plus_modify_pwd_confirm_iv);
		oldPwd_et.setOnFocusChangeListener(this);
		newPwd_et.setOnFocusChangeListener(this);
		confirm_et.setOnFocusChangeListener(this);
		motify_btn.setOnClickListener(this);
	}

	@Override
	public void onFinishUpdatePwd(int reflag) {
		Log.e("xuxuxu", "onFinishUpdatePwd:" + reflag);
		progressDialog.dismiss();
		switch (reflag) {
		case 5:
			Toast.makeText(this, "密码修改成功！", Toast.LENGTH_SHORT).show();
			finish();
			break;
		case 1:
		case 103:
			Toast.makeText(this, "旧密码不正确！", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	public void onErrorUpdatePwd(Exception e) {
		Log.e("xuxuxu", "onErrorUpdatePwd:" + e.getMessage());
		Toast.makeText(this, "密码修改失败！", Toast.LENGTH_SHORT).show();
		progressDialog.dismiss();
	}

	@Override
	public void onClick(View view) {
		if(oldPwd_et.getText().toString().trim().length()==0){
			Toast.makeText(this, "原始密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if(newPwd_et.getText().toString().trim().length()==0){
			Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
			return;
		}
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(newPwd_et.getText().toString());
		Pattern p1 = Pattern.compile("[a-zA-Z]*");
		Matcher m1 = p1.matcher(newPwd_et.getText().toString());
		Pattern p2 = Pattern.compile("[a-z0-9A-Z\\.\\_]*");
		Matcher m2 = p2.matcher(newPwd_et.getText().toString());
		int s=newPwd_et.getText().toString().length();
		if (newPwd_et.getText().toString()
				.equals(confirm_et.getText().toString())) {
			if(!m.matches() && !m1.matches() && m2.matches()){
				if (s >= 6 && s <= 12) {
					new UpdatePwdTask(HealthPlusModifyPwdActivity.this, oldPwd_et
							.getText().toString().trim(), newPwd_et.getText()
							.toString().trim(), HealthPlusModifyPwdActivity.this)
							.execute();
					progressDialog.show();
				}else{
					Toast.makeText(this, "新密码长度不符", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "密码必须是由英文字母、数字或符号组成", Toast.LENGTH_SHORT).show();
			}
		
		}else{
			Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.health_plus_modify_pwd_old_et:
			if (hasFocus) {
				old_img.setImageResource(R.drawable.health_plus_pwd_focus);
			} else {
				old_img.setImageResource(R.drawable.health_plus_pwd_normal);
			}
			break;

		case R.id.health_plus_modify_pwd_new_et:
			if (hasFocus) {
				new_img.setImageResource(R.drawable.health_plus_pwd_focus);
			} else {
				new_img.setImageResource(R.drawable.health_plus_pwd_normal);
			}
			break;
		case R.id.health_plus_modify_pwd_confirm_et:
			if (hasFocus) {
				confirm_img.setImageResource(R.drawable.health_plus_pwd_focus);
			} else {
				confirm_img.setImageResource(R.drawable.health_plus_pwd_normal);
			}
			break;

		}

	}

}
