package com.vee.healthplus.ui.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.UpdatePwdTask;
import com.vee.healthplus.widget.CustomDialog;
import com.vee.healthplus.widget.CustomProgressDialog;

/**
 * Created by wangjiafeng on 13-11-18.
 */
public class UserPwdMotify extends DialogFragment implements UpdatePwdTask.UpdatePwdCallBack, View.OnClickListener {

    private EditText oldPwd_et, newPwd_et;
    private CustomProgressDialog progressDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setTitle(R.string.hp_userinfo_pwdmotify);
        View layout = View.inflate(getActivity(), R.layout.userpwdmotify, null);
        initView(layout);
        builder.setContentView(layout);
        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        progressDialog = CustomProgressDialog.createDialog(this.getActivity());
        progressDialog.setMessage(this.getString(R.string.changing_pwd));
    }

    private void initView(View view) {
        oldPwd_et = (EditText) view.findViewById(R.id.oldPwd_et);
        newPwd_et = (EditText) view.findViewById(R.id.newPwd_et);
        Button motify_btn = (Button) view.findViewById(R.id.motify_btn);
        motify_btn.setOnClickListener(this);
    }

    @Override
    public void onFinishUpdatePwd(int reflag) {
        Log.e("xuxuxu", "onFinishUpdatePwd:" + reflag);
        progressDialog.dismiss();
        switch (reflag) {
            case 5:
                Toast.makeText(getActivity(), "密码修改成功！", Toast.LENGTH_SHORT).show();
                this.dismiss();
                break;
            case 1:
            case 103:
                Toast.makeText(getActivity(), "密码修改失败！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onErrorUpdatePwd(Exception e) {
        Log.e("xuxuxu", "onErrorUpdatePwd:" + e.getMessage());
        Toast.makeText(getActivity(), "密码修改失败！", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        new UpdatePwdTask(UserPwdMotify.this.getActivity(), oldPwd_et.getText().toString().trim(), newPwd_et.getText().toString().trim(), UserPwdMotify.this).execute();
        progressDialog.show();
    }

}
