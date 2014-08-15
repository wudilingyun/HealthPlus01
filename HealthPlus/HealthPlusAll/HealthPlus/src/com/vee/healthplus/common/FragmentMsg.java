package com.vee.healthplus.common;

import java.util.Calendar;

import android.support.v4.app.Fragment;


public class FragmentMsg {

    public final static int CALENDAR_PICKER_TO_CALENDAR = 1;

    public final static int CALENDER_TO_INFO = 2;

    public final static int INFO_TO_CALENDER = 3;

    private int flag;

    private Fragment objFragment;

    private Fragment srcFragment;

    private int animIn;

    private int animOut;


    private Calendar pickCalen;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Fragment getObjFragment() {
        return objFragment;
    }

    public void setObjFragment(Fragment fragment) {
        this.objFragment = fragment;
    }

    public Fragment getSrcFragment() {
        return srcFragment;
    }

    public void setSrcFragment(Fragment srcFragment) {
        this.srcFragment = srcFragment;
    }

    public int getAnimIn() {
        return animIn;
    }

    public void setAnimIn(int animIn) {
        this.animIn = animIn;
    }

    public int getAnimOut() {
        return animOut;
    }

    public void setAnimOut(int animOut) {
        this.animOut = animOut;
    }

    public Calendar getPickCalen() {
        return pickCalen;
    }

    public void setPickCalen(Calendar pickCalen) {
        this.pickCalen = pickCalen;
    }
}
