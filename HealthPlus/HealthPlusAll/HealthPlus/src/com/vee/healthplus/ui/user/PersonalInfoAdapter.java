package com.vee.healthplus.ui.user;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vee.healthplus.util.AppPreferencesUtil;

public class PersonalInfoAdapter extends BaseAdapter {

	private Context context;
	protected ArrayList<ListElement> infoList;
	private LayoutInflater layoutInflater;

	public PersonalInfoAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService("layout_inflater");
		this.infoList = new ArrayList<ListElement>();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return infoList.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		return this.infoList.get(position).getViewForListElement(
				layoutInflater, context, convertView);

	}

	public void setList(ArrayList<ListElement> infoList) {
		this.infoList = infoList;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return super.isEnabled(position);
	}

}
