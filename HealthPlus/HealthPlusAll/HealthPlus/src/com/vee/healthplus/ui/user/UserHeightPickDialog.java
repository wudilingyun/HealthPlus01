package com.vee.healthplus.ui.user;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;
import com.vee.healthplus.widget.CustomDialog;

/**
 * Created by zhou on 13-10-28.
 */
public class UserHeightPickDialog extends DialogFragment implements View.OnClickListener {
    private WheelView age_wv;
    @SuppressLint("ValidFragment")
	private HP_User user;
    private ICallBack callBack;

    @SuppressLint("ValidFragment")
	public UserHeightPickDialog(HP_User user, ICallBack callBack) {
        this.user = user;
        this.callBack = callBack;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setTitle(R.string.hp_userinfotitle_height);
        View layout = View.inflate(getActivity(), R.layout.hp_userinfodialog_age, null);
        initView(layout);
        builder.setContentView(layout);
        return builder.create();
    }

    private void initView(View layout) {
        age_wv = (WheelView) layout.findViewById(R.id.age_wv);
        age_wv.setViewAdapter(new Age_Adapter(getActivity()));
        age_wv.setCurrentItem((int) user.userHeight - 1);
        Button setDistanceTarget_btn = (Button) layout.findViewById(R.id.ok_btn);
        setDistanceTarget_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_btn:
                user.userHeight = age_wv.getCurrentItem() + 1;
                HP_DBModel.getInstance(getActivity()).updateUserInfo(user, true);
                callBack.onChange();
                this.dismiss();
                break;
        }
    }

    private class Age_Adapter extends AbstractWheelTextAdapter {

        private int maxAge = 250;

        protected Age_Adapter(Context context) {
            super(context);
        }

        @Override
        protected CharSequence getItemText(int index) {
            return String.valueOf(index + 1);
        }

        @Override
        public int getItemsCount() {
            return maxAge;
        }
    }
}
