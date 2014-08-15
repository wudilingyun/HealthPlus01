package com.vee.moments;

import java.util.HashMap;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.ExpiredAuthorizationException;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.ResourceAccessException;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.user.HP_User;
import com.vee.moments.adapter.ContactAdapter;
import com.yunfox.s4aservicetest.response.PhoneContactsResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

/**
 * @author wangdongsheng
 * 
 */
public class AddContsctsActivity extends FragmentActivity implements
		OnQueryTextListener, OnItemClickListener, OnClickListener {
	private static final String TAG = "AddContsctsActivity";
	private ListView mContactsListview;
	private ImageView loadImageView;
	private AsyncQueryHandler mAsyncQueryHandler;
	private LinearLayout loFrameLayout;
	private static final int LOAD = 1;
	private String contactArry[];
	private List<PhoneContactsResponse> mContactsList;
	private HashMap<String, String> data;
	private Animation news_loadAaAnimation;
	private TextView contactNone;
	private TextView header_text;
	private ImageView header_lbtn_img, header_rbtn_img;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			switch (msg.what) {

			case LOAD:
				if (mContactsList != null && mContactsList.size() != 0
						&& data != null && data.size() != 0) {
					setAdapter(mContactsList, data);
				} else {
					if (HP_User.getOnLineUserId(AddContsctsActivity.this) == 0)
						contactNone.setVisibility(View.GONE);
				}
				loadImageView.clearAnimation();
				loFrameLayout.setVisibility(View.GONE);
				break;

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_contacts_list, null);
		setContentView(view);
		gettitle();

		contactNone = (TextView) findViewById(R.id.contacts_list_none);
		loFrameLayout = (LinearLayout) findViewById(R.id.loading_frame);
		mContactsListview = (ListView) findViewById(R.id.contacts_list);
		news_loadAaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.wait_heart_result);
		loadImageView = (ImageView) findViewById(R.id.img_rotate);
		loadImageView.setAnimation(news_loadAaAnimation);
		loFrameLayout.setVisibility(View.VISIBLE);
		loadImageView.startAnimation(news_loadAaAnimation);
		mContactsListview.setOnItemClickListener(this);
		mAsyncQueryHandler = new ContactsAsyncQueryHandler(getContentResolver());
		startQuery();

	}

	void gettitle() {

		header_text = (TextView) findViewById(R.id.header_text);
		header_lbtn_img = (ImageView) findViewById(R.id.header_lbtn_img);
		header_rbtn_img = (ImageView) findViewById(R.id.header_rbtn_img);
		header_rbtn_img.setVisibility(View.GONE);
		header_text.setText("添加手机联系人");
		header_text.setOnClickListener(this);
		header_lbtn_img.setOnClickListener(this);
	}

	private LayoutAnimationController creatAnimation() {
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(50);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(100);
		set.addAnimation(animation);

		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		return controller;
	}

	private void startQuery() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY }; // 查询的列
		mAsyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询

	}

	// @Override
	// public void onCreateOptionsMenu(Menu menu) {
	// Place an action bar item for searching.
	// MenuItem item = menu.add("Search");
	// item.setIcon(android.R.drawable.ic_menu_search);
	// item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
	// | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	// SearchView sv = new SearchView(this);
	// sv.setOnQueryTextListener(this);
	// item.setActionView(sv);
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 数据库异步查询类AsyncQueryHandler
	 * 
	 * @author wangdongsheng
	 * 
	 */
	private class ContactsAsyncQueryHandler extends AsyncQueryHandler {

		public ContactsAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函数
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			Log.i("lingyun", "AddContsctsActivity.onQueryComplete");
			if (cursor != null && cursor.getCount() > 0) {

				cursor.moveToFirst();
				contactArry = new String[cursor.getCount()];
				data = new HashMap<String, String>();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);
					data.put(number, name);
					// contactArry=new String[]{name,number};

					contactArry[i] = number;

					// if (number.startsWith("+86")) {//
					// 去除多余的中国地区号码标志，对这个程序没有影响。
					// cb.setPhoneNum(number.substring(3));
					// } else {

					// }
				}

				if (contactArry.length > 0) {

					Log.d(TAG, contactArry.length + "");

					LoadThread mLoadThread = new LoadThread();
					mLoadThread.start();
				}
			} else {
				contactNone.setVisibility(View.VISIBLE);
				loadImageView.clearAnimation();
				loFrameLayout.setVisibility(View.GONE);
			}
		}
	}

	public void setAdapter(List<PhoneContactsResponse> list,
			HashMap<String, String> data) {
		ContactAdapter hc = new ContactAdapter(this, list, data);
		mContactsListview.setAdapter(hc);

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	class LoadThread extends Thread {

		public void run() {
			try {
				mContactsList = SpringAndroidService.getInstance(
						getApplication()).queryPhoneContacts(contactArry);
			} catch (Exception exception) {
				if (exception != null) {
					String message = "";
					if (exception instanceof DuplicateConnectionException) {
						message = "The connection already exists.";
						Log.e("lingyun", "queryPhoneContacts.error=" + message);
					} else if (exception instanceof ResourceAccessException
							&& exception.getCause() instanceof ConnectTimeoutException) {
						message = "connect time out";
						Log.e("lingyun", "queryPhoneContacts.error=" + message);
					} else if (exception instanceof MissingAuthorizationException) {
						message = "please login first";
						Log.e("lingyun", "queryPhoneContacts.error=" + message);
						SpringAndroidService.getInstance(getApplication())
								.signOut();
						startActivity(new Intent(AddContsctsActivity.this,
								HealthPlusLoginActivity.class));
						finish();

					} else if (exception instanceof ExpiredAuthorizationException) {
						message = "authorization expired";
						Log.e("lingyun", "queryPhoneContacts.error=" + message);
						SpringAndroidService.getInstance(getApplication())
								.signOut();
						startActivity(new Intent(AddContsctsActivity.this,
								HealthPlusLoginActivity.class));
						finish();
					} else {
						message = "A problem occurred with the network connection. Please try again in a few minutes.";
						Log.e("lingyun", "queryPhoneContacts.error=" + message);
					}
					System.out.println(message);
				}

			}

			Message msg = Message.obtain();
			msg.what = LOAD;
			handler.sendMessage(msg);

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Toast.makeText(this, "to  FriendDetailActivity", Toast.LENGTH_SHORT)
		// .show();
		// Intent intent = new Intent(this, FriendDetailActivity.class);
		//
		// startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}

}
