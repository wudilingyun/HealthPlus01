package com.vee.shop.ui;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.util.user.ICallBack;
import com.vee.shop.bean.AddressBean;
import com.vee.shop.http.GetCartTask;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.vee.shop.util.TextUtil;
import com.vee.shop.util.ToastUtil;
import com.yunfox.s4aservicetest.response.CartAddressList;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class AddressListFragment extends BaseFragment implements ICallBack {
	private static final String TAG = "AddressListFragment";
	private RelativeLayout address_add_containerLayout;
	private ListView mListView;
	private myListAdapter mListAdapter;
	private ArrayList<AddressBean> addressList;
	private TextView address_no;
	OnAddressChangeListener mOnAddressChangeListener;
	private final static int REQUEST_CODE_LOGIN = 100;
	private String backupJson;
	AlertDialog.Builder delDialog;
	private int scrollPos;
	private int scrollTop;

	public interface OnAddressChangeListener {
		public void OnAddressChange(AddressBean addressBean);

		public void OnAddressChoose(int position);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnAddressChangeListener = (OnAddressChangeListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addressList = new ArrayList<AddressBean>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView = inflater.inflate(ApplicationUtils.getResId("layout",
				"shop_address_list_fragment"), container, false);
		address_no = (TextView) localView.findViewById(ApplicationUtils
				.getResId("id", "address_no"));
		address_add_containerLayout = (RelativeLayout) localView
				.findViewById(ApplicationUtils.getResId("id",
						"add_address_bottom"));
		address_add_containerLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOnAddressChangeListener.OnAddressChange(null);
			}
		});
		mListView = (ListView) localView.findViewById(ApplicationUtils
				.getResId("id", "address_listfragment_listview"));
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (null != addressList.get(position))
					mOnAddressChangeListener.OnAddressChange(addressList
							.get(position));
				mOnAddressChangeListener.OnAddressChoose(position);
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (null != addressList.get(position)) {
					final String url = addressList.get(position).getDeleteUrl();
					final int pos = position;
					delDialog = new AlertDialog.Builder(mContext);
					// delDialog.setTitle(addressList.get(position).getReceiver()
					// + "  " + addressList.get(position).getMobile());
					delDialog.setMessage(getString(ApplicationUtils.getResId(
							"string", "order_del_message")));
					delDialog.setPositiveButton(
							ApplicationUtils.getResId("string", "Ensure"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// myHttpPostAsyncTask delAddressTask = new
									// myHttpPostAsyncTask(
									// url, null, mContext, pos);
									// delAddressTask.execute();
									new myDelAddressTask(url, null, mContext,
											pos).execute();
								}
							});
					delDialog.setNegativeButton(
							ApplicationUtils.getResId("string", "Cancel"),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							});
					delDialog.show();
				}
				return true;
			}
		});
		mListAdapter = new myListAdapter();
		return localView;
	}

	@Override
	public void onResume() {
		super.onResume();
		backupJson = settings.getString("AddresslistJsonString", "null");
		new myGetAddressTask(Constants.ACCOUNT_QUERY_ADDRESS_URL, null,
				mContext).execute();
	}

	class myListAdapter extends BaseAdapter {

		Holder holder;

		class Holder {
			TextView address_area;
			TextView address;
			TextView address_consignee;
		}

		@Override
		public int getCount() {
			return addressList.size();
		}

		@Override
		public Object getItem(int item) {
			return item;
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final AddressBean ab = addressList.get(position);
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(mContext).inflate(
						ApplicationUtils.getResId("layout",
								"shop_address_list_item"), null);
				holder.address_area = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"address_area"));
				holder.address = (TextView) convertView
						.findViewById(ApplicationUtils
								.getResId("id", "address"));
				holder.address_consignee = (TextView) convertView
						.findViewById(ApplicationUtils.getResId("id",
								"address_consignee"));
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.address_area.setText(ab.getProvince() + " " + ab.getCity()
					+ " " + ab.getDistrict());
			holder.address.setText(ab.getDetail() + " " + ab.getPostcode());
			holder.address_consignee.setText(ab.getReceiver() + " "
					+ ab.getMobile());
			return convertView;
		}

	}

	class myGetAddressTask extends ProtectTask {

		CartAddressList serverAddressList;

		public myGetAddressTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			getDialog().show();
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				serverAddressList = (SpringAndroidService
						.getInstance(getActivity().getApplication())
						.handleProtectAddress(actionUrl, formData,
								Constants.HTTP_TYPE_GET));
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
			}
			return super.doInBackground(params);
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			address_add_containerLayout.setVisibility(View.VISIBLE);
			if (exception != null) {
				if (exception instanceof HttpClientErrorException) {
					HttpClientErrorException httpError = (HttpClientErrorException) exception;
					if (httpError.getStatusCode() == HttpStatus.UNAUTHORIZED) {
						// go login
						address_add_containerLayout
								.setVisibility(View.INVISIBLE);

					}
				} else if (exception instanceof MissingAuthorizationException) {
					// go login
					address_add_containerLayout.setVisibility(View.INVISIBLE);

				}
			}
			String json;
			if ((exception == null) && (serverAddressList != null)) {
				json = TextUtil.createAddressJson(serverAddressList);
				editor.putString("AddresslistJsonString", json);
				editor.commit();
			} else {
				// json = backupJson;
				json = null;
			}
			addressList = httpUtil.parseAddressList(json);
			if ((null != addressList) && (addressList.size() > 0)) {
				mListView.setAdapter(mListAdapter);
				ApplicationUtils.setListViewHeightBasedOnChildren(mListView);
				address_no.setVisibility(View.GONE);
			} else {
				address_no.setVisibility(View.VISIBLE);
			}
		}

	}

	class myDelAddressTask extends ProtectTask {

		int position;

		public myDelAddressTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		public myDelAddressTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext,
				int position) {
			super(actionUrl, formData, mContext);
			this.position = position;
		}

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				return (SpringAndroidService.getInstance(getActivity()
						.getApplication()).handleProtect(actionUrl, formData,
						Constants.HTTP_TYPE_POST));
			} catch (Exception e) {
				Log.e(TAG, e.getLocalizedMessage(), e);
				this.exception = e;
				return null;
			}
		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			if ((!TextUtils.isEmpty(result.toString()))
					&& (!result.toString().equals("null"))) {
				String returncode = httpUtil.parseServerResponse(result
						.toString());
				if ("200".equals(returncode)) {
					addressList.remove(position);
					mListAdapter.notifyDataSetChanged();
				} else {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "order_submit_exception_send_data"));
				}
			}
		}
	}

	@Override
	public void onChange() {
		new myGetAddressTask(Constants.ACCOUNT_QUERY_ADDRESS_URL, null,
				mContext).execute();
		new GetCartTask(null, null, mContext, getActivity()).execute();
	}

	@Override
	public void onCancel() {
		getActivity().finish();
	}

}
