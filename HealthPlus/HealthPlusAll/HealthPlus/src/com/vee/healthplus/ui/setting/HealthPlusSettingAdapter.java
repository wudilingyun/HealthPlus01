package com.vee.healthplus.ui.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;

public class HealthPlusSettingAdapter extends BaseAdapter {

	private Context ctx;

	private String itemName[] = new String[] { "版本更新", "建议反馈", "分享给好友", "关于应用" };

	private int itemIcon[] = new int[] {
			R.drawable.health_plus_setting_update_icon,
			R.drawable.health_plus_setting_advice_icon,
			R.drawable.health_plus_setting_share_icon,
			R.drawable.health_plus_setting_about_icon };

	@SuppressWarnings("rawtypes")
	/*public static Class clazz[] = new Class[] { Achievement.class,
			Achievement.class,Achievement.class,
			Achievement.class, SettingFeedBack.class, AboutActivity.class };*/

	protected LayoutInflater _mInflater;

	public HealthPlusSettingAdapter(Context ctx) {
		this.ctx = ctx;
		_mInflater = LayoutInflater.from(this.ctx);
	}

	public void notifyDataChange() {
		this.notifyDataSetChanged();
	}

	public int getCount() {
		return itemName.length;
	}

	public Object getItem(int position) {
		return itemName[position];
	}

	public long getItemId(int position) {
		return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = _mInflater.inflate(
					R.layout.healthplus_setting_list_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.health_plus_setting_list_item_icon_iv);
			holder.name = (TextView) convertView
					.findViewById(R.id.health_plus_setting_list_item_name_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.icon.setImageResource(itemIcon[position]);
		holder.name.setText(itemName[position]);

		return convertView;
	}

	public class ViewHolder {
		ImageView icon;
		TextView name;

	}

}
