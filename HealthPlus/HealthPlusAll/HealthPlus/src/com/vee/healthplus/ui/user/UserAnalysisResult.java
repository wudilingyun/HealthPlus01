package com.vee.healthplus.ui.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.UserIndexUtils;
import com.vee.healthplus.widget.CustomDialog;

/**
 * Created by wangjiafeng on 13-11-25.
 */
public class UserAnalysisResult extends DialogFragment implements View.OnClickListener {

    private TextView result_result, result_idealweight, result_bmi, result_bmr, result_everydayquality, result_addweight, result_subtrackweight;
    @SuppressLint("ValidFragment")
	private HP_User user;
    private Context mContext;

    @SuppressLint("ValidFragment")
	public UserAnalysisResult(HP_User user) {
        this.user = user;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.mContext = getActivity();
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setTitle(R.string.hp_userinfo_analysisresult);
        View view = View.inflate(getActivity(), R.layout.useranalysisdialog, null);
        initView(view);
        builder.setContentView(view);
        return builder.create();
    }

    private void initView(View view) {
        result_result = (TextView) view.findViewById(R.id.result_result);
        result_idealweight = (TextView) view.findViewById(R.id.result_idealweight);
        result_bmi = (TextView) view.findViewById(R.id.result_bmi);
        result_bmr = (TextView) view.findViewById(R.id.result_bmr);
        result_everydayquality = (TextView) view.findViewById(R.id.result_everydayquality);
        result_addweight = (TextView) view.findViewById(R.id.result_addweight);
        result_subtrackweight = (TextView) view.findViewById(R.id.result_subtrackweight);
        Button back_btn = (Button) view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        analysisUserInfo();
    }

    private void analysisUserInfo() {
        result_result.setText(UserIndexUtils.getResult(mContext, user));
        result_idealweight.setText(UserIndexUtils.getIdealWeight(mContext, user));
        result_bmi.setText(UserIndexUtils.getUserBMI(mContext, user));
        result_bmr.setText(UserIndexUtils.getUserBMR(mContext, user));
        result_everydayquality.setText(UserIndexUtils.getEveryDayQuality(mContext, user));
        result_addweight.setText(UserIndexUtils.getAddWeight(mContext, user));
        result_subtrackweight.setText(UserIndexUtils.getSubtrackWeight(mContext, user));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                dismiss();
                break;
        }
    }
}
