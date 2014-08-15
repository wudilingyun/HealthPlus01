package com.vee.healthplus.util.user;

import android.R.string;
import android.content.Context;

import com.vee.healthplus.util.SharedPreferenceUtil;

/**
 * Created by wangjiafeng on 13-10-24.
 */
public class HP_User {
    public int userId;
    public String userName = "";
    public int userAge;
    public int userHeight;
    public int userSex = -1;
    public float userWeight;
    public long updateTime;
    public String userNick = "";
    public String email = "";
    public String phone = "";
    public String remark = "";
    public String photourl="";
    public static int getOnLineUserId(Context mContext) {
        return SharedPreferenceUtil.getIntPref(mContext, "userId", 0);
    }

    public static void setOnLineUserId(Context mContext, int id) {
        SharedPreferenceUtil.setIntPref(mContext, "userId", id);
    }

}
