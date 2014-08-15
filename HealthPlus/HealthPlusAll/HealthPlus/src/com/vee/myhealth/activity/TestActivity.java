package com.vee.myhealth.activity;

import java.util.List;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.main.QuitDialog;
import com.vee.myhealth.bean.HealthQuestionEntity;
import com.vee.myhealth.util.MyHealthAdapter;
import com.vee.myhealth.util.SqlDataCallBack;
import com.vee.myhealth.util.SqlForTest;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TestActivity extends FragmentActivity implements
		SqlDataCallBack<HealthQuestionEntity>, OnCheckedChangeListener {
	private ExpandableListView examListView;
	private SqlForTest sqlForTest;
	private List<HealthQuestionEntity> heList;
	private Button submit_butt;
	private MyHealthAdapter testadpter;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		View view = View.inflate(this, R.layout.myhealth_expandablelistview, null);
		setContentView(view);
		super.onCreate(arg0);
		init();
		getDataFromSQL();
	}

	void init() {
		examListView = (ExpandableListView) findViewById(R.id.healthtest_item);
		testadpter = new MyHealthAdapter(this);
	}

	void getDataFromSQL() {
		sqlForTest = new SqlForTest(this);
		sqlForTest.getHealthContent("113");
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		QuitDialog qd = new QuitDialog(true,"提示");
		qd.show(this.getSupportFragmentManager(), "exam");
	}
	@Override
	public void getData(List<HealthQuestionEntity> test) {
		// TODO Auto-generated method stub
		heList = test;
		testadpter.addList(heList);
		examListView.setAdapter(testadpter);
		testadpter.notifyDataSetChanged();
	}

	@Override
	public void getResult(Object c) {
		// TODO Auto-generated method stub
		
	}
	
}
