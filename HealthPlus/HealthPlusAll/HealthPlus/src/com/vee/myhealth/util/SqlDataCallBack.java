package com.vee.myhealth.util;

import java.util.List;

import com.vee.myhealth.bean.TZtest;

public interface SqlDataCallBack<T> {
	public void getData(List<T> test);

	public void getResult(Object c);
}
