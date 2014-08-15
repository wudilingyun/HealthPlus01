package com.vee.myhealth.util;

import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.vee.healthplus.R;
import com.vee.myhealth.bean.HealthQuestionEntity;
import com.yunfox.s4aservicetest.response.Exam;

import android.Manifest.permission;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MyHealthAdapter extends BaseExpandableListAdapter {
	LayoutInflater inflater;
	private Context context;
	List<HealthQuestionEntity> testlist;

	public MyHealthAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void addList(List<HealthQuestionEntity> list) {
		this.testlist = list;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return ((HealthQuestionEntity) testlist.get(groupPosition));
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = null;

		if (convertView != null) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.myhealth_testchild_item, parent,
					false);

		}
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return testlist.get(groupPosition).getQuestion();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return testlist.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.health_item_examtype, parent,
				false);
		TextView ques_tView = (TextView) view.findViewById(R.id.txt_examtype);
		ques_tView.setText(testlist.get(groupPosition).getQuestion());

		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
