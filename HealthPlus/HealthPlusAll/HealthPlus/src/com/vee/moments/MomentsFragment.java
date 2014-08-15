package com.vee.moments;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_utils.CheckNetWorkStatus;
import com.vee.healthplus.http.StatisticsUtils;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.SystemMethod;
import com.vee.healthplus.util.user.HP_User;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class MomentsFragment extends Fragment {
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 10) {
			Bundle bundle = data.getExtras();
			String photopath = bundle.getString("photopath");
			Intent intent = new Intent(getActivity(), NewMomentsActivity.class);
			intent.putExtra("bitmap", photopath);
			startActivity(intent);
		} else if (requestCode == 12) {
			if (resultCode == getActivity().RESULT_OK) {
				Bundle bundle = data.getExtras();
				String coverpath = bundle.getString("coverpath");
				String cover = bundle.getString("cover");
				String coverpath2 = Uri.parse(cover).getPath();
				Bitmap head;
				try {
					head = MediaStore.Images.Media.getBitmap(this.getActivity()
							.getContentResolver(), Uri.parse(cover));
					if (imageViewCoverDefault != null) {
						imageViewCoverDefault.setImageBitmap(head);
					}
					if (imageViewCoverList != null) {
						imageViewCoverList.setImageBitmap(head);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static MomentsFragment newInstance() {
		return new MomentsFragment();
	}

	RelativeLayout relativeLayoutNoMents;
	RelativeLayout relativeLayoutMomentsHeading;
	ListView listViewMonentsList;
	private ImageView imageViewCoverDefault = null;
	private ImageView imageViewCoverList = null;

	private static String[] mTitles;
	private MomentsMainAdapter momentsMainAdapter;
	private TextView textViewNewFriendUnread = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.moments_fragment, container,
				false);

		ListView listViewMomentsMain = (ListView) view
				.findViewById(R.id.momentsmain);
		mTitles = SystemMethod.getStringArray(this.getActivity(),
				R.array.momentsmain);
		momentsMainAdapter = new MomentsMainAdapter(getActivity(), mTitles);
		listViewMomentsMain.setAdapter(momentsMainAdapter);

		listViewMomentsMain.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (!CheckNetWorkStatus.Status(getActivity())) {
					Toast.makeText(getActivity(), "网络不给力啊", Toast.LENGTH_SHORT)
							.show();
				} else {
					if (HP_User.getOnLineUserId(getActivity()) == 0) {
						Intent intent = new Intent(getActivity(),
								HealthPlusLoginActivity.class);
						/*
						 * Bundle extras = new Bundle(); extras.putParcelable(
						 * "cn", new ComponentName("com.vee.healthplus",
						 * "com.vee.healthplus.ui.heahth_news.Health_ValueBook_commentList_activity"
						 * )); intent.putExtras(extras);
						 */
						getActivity().startActivity(intent);

					} else {

						if (position == 0) {
							if (HP_User.getOnLineUserId(getActivity()) == 0) {
								Toast.makeText(getActivity(), "请先 登录",
										Toast.LENGTH_SHORT).show();
							} else {
								if (!CheckNetWorkStatus.Status(getActivity())) {
									Toast.makeText(getActivity(), "网络不给力啊",
											Toast.LENGTH_SHORT).show();
								} else {
									Intent intent = new Intent(
											MomentsFragment.this.getActivity(),
											MomentsMainActivity.class);
									StatisticsUtils.startFunction(StatisticsUtils.FRIEND_JKQ_ID);
									startActivity(intent);
								}

							}

						} else if (position == 1) {
							Intent intent = new Intent(MomentsFragment.this
									.getActivity(), FriendListActivity.class);
							startActivity(intent);
						} 
						
						/*else if (position == 2) {
							textViewNewFriendUnread.setVisibility(View.GONE);
							Intent intent = new Intent(MomentsFragment.this
									.getActivity(), NewFriendListActivity.class);
							startActivity(intent);
						} */
						
						else if (position == 2) {
							Intent intent = new Intent(MomentsFragment.this
									.getActivity(), SearchPhoneActivity.class);
							startActivityForResult(intent, 1);
						} else if (position == 3) {
							Intent intent = new Intent(MomentsFragment.this
									.getActivity(), AddContsctsActivity.class);
							startActivity(intent);
						}
					}
				}
			}
		});

		String uname = String.valueOf(SpringAndroidService.getInstance(
				getActivity().getApplication()).getMyId());
		System.out.println("uname====" + uname);
		//startICometService(uname);

		return view;
	}

	private class MomentsMainAdapter extends BaseAdapter {
		private Context mContext;
		private String[] sTitles;
		private int icons[] = { R.drawable.moments_main_moments,
				R.drawable.moments_main_friendlist,
				//R.drawable.moments_main_newfriend,
				R.drawable.moments_main_searchphone,
				R.drawable.moments_main_addcontact };

		protected LayoutInflater _mInflater;

		public MomentsMainAdapter(Context mContext, String[] strings) {
			super();
			this.mContext = mContext;
			this.sTitles = strings;
			_mInflater = LayoutInflater.from(this.mContext);
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			if (position == 0) {
				return 0;
			}
			return 1;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sTitles.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			int type = getItemViewType(position);
			final ViewHolder holder;
			if (convertView == null) {
				/*
				 * if( type == 1) { convertView =
				 * _mInflater.inflate(R.layout.moments_fragment_listview_item,
				 * null); } else { convertView =
				 * _mInflater.inflate(R.layout.moments_fragment_listview_firstitem
				 * , null); }
				 */
				convertView = _mInflater.inflate(
						R.layout.moments_fragment_listview_item, null);

				holder = new ViewHolder();
				holder.icon = (ImageView) convertView
						.findViewById(R.id.addfriendimage);
				holder.title = (TextView) convertView
						.findViewById(R.id.addfriendname);
				convertView.setTag(holder);
				if (position == 2) {
					textViewNewFriendUnread = (TextView) convertView
							.findViewById(R.id.new_friend_unread);
				}
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(sTitles[position]);
			holder.icon.setImageResource(icons[position]);
			return convertView;
		}

		class ViewHolder {
			ImageView icon;
			TextView title;
		}
	}
}
