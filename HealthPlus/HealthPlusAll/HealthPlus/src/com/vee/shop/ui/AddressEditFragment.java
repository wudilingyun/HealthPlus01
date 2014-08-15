package com.vee.shop.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.vee.shop.bean.AddressBean;
import com.vee.shop.bean.AddressInfo;
import com.vee.shop.http.ProtectTask;
import com.vee.shop.http.httpUtil;
import com.vee.shop.util.ApplicationUtils;
import com.vee.shop.util.Constants;
import com.vee.shop.util.LogUtil;
import com.vee.shop.util.ToastUtil;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

public class AddressEditFragment extends BaseFragment {
	private static final String TAG = "AddressEditFragment";
	private Spinner spPro;
	private Spinner spCity;
	private Spinner spDistrict;

	private ArrayList<AddressInfo> mAdressList;
	private ProvinceAdapter spProAdp;
	private CityAdapter spCityAdp;
	private DistrictAdapter spDistrictAdp;
	private EditText mConsignee;
	private EditText mZipcode;
	private EditText mLocation;
	private EditText mTel;
	private Button butSubmit;
	private AddressBean addressBean = null;
	private HashMap<Integer, AddressInfo> provinceMap = new HashMap<Integer, AddressInfo>();
	private HashMap<Integer, AddressInfo> cityMap = new HashMap<Integer, AddressInfo>();
	private HashMap<Integer, AddressInfo> disMap = new HashMap<Integer, AddressInfo>();
	OnAddSucListener mOnAddSucListener;
	OnEditTextListener mOnEditTextListener;

	private boolean forChange = true;
	private boolean onCreate = true;
	private String zipcodeString = "100000";
	private int itemSelectedCount = 0;

	public interface OnAddSucListener {
		public void OnAddSuc();
	}

	public interface OnEditTextListener {
		public void OnEtSet(EditText e1, EditText e2, EditText e3, EditText e4);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mOnAddSucListener = (OnAddSucListener) activity;
			mOnEditTextListener = (OnEditTextListener) activity;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (null != bundle.getParcelable("address_list_item_choosed")) {
			addressBean = bundle.getParcelable("address_list_item_choosed");
		} else {
			addressBean = new AddressBean();
			forChange = false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View localView1 = inflater.inflate(ApplicationUtils.getResId("layout",
				"shop_address_edit_fragment"), container, false);
		loadAddressInfo();
		spPro = (Spinner) localView1.findViewById(ApplicationUtils.getResId(
				"id", "address_province"));
		spProAdp = new ProvinceAdapter();
		spProAdp.setPatentAddr(1);
		spPro.setAdapter(spProAdp);
		spCity = (Spinner) localView1.findViewById(ApplicationUtils.getResId(
				"id", "address_city"));
		spCityAdp = new CityAdapter();
		spCity.setAdapter(spCityAdp);
		spDistrict = (Spinner) localView1.findViewById(ApplicationUtils
				.getResId("id", "address_district"));
		spDistrictAdp = new DistrictAdapter();
		spDistrict.setAdapter(spDistrictAdp);
		mZipcode = (EditText) localView1.findViewById(ApplicationUtils
				.getResId("id", "address_zipcode"));
		mConsignee = (EditText) localView1.findViewById(ApplicationUtils
				.getResId("id", "address_consignee"));
		mLocation = (EditText) localView1.findViewById(ApplicationUtils
				.getResId("id", "address_location"));
		mTel = (EditText) localView1.findViewById(ApplicationUtils.getResId(
				"id", "address_tel"));
		spDistrict.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// LogUtil.d(TAG, "onItemSelected "+
				// itemSelectedCount);
				// LogUtil.d(TAG, forChange+" "+onCreate);
				if (forChange) {
					if (!onCreate) {
						mZipcode.setText(zipcodeString);
					}
					if (itemSelectedCount > 0)
						onCreate = false;
				} else {
					mZipcode.setText(zipcodeString);
				}
				itemSelectedCount++;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				LogUtil.d(TAG, "onNothingSelected");
				mZipcode.setText("");

			}
		});
		butSubmit = (Button) localView1.findViewById(ApplicationUtils.getResId(
				"id", "address_submit"));
		butSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String zipcode = mZipcode.getText().toString().trim();
				String consignee = mConsignee.getText().toString().trim();
				String location = mLocation.getText().toString().trim();
				String telephone = mTel.getText().toString().trim();

				// Pattern enPattern = Pattern.compile("[a-zA-Z]");
				// Pattern chPattern = Pattern.compile("[\u4e00-\u9fa5]");
				Log.d(TAG,
						consignee
								+ " name pattern is chinese "
								+ Pattern
										.matches("[\u4e00-\u9fa5]+", consignee));
				if (consignee == null || consignee.length() == 0
						|| !Pattern.matches("[\u4e00-\u9fa5]+", consignee)) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "tips_address_consignee"));
				} else if (location == null || location.length() < 5) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "tips_address_location"));
				} else if (zipcode == null || zipcode.length() != 6) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "tips_address_zipcode_length"));
				} else if (telephone == null || telephone.length() != 11) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "tips_address_tel"));
				} else if (null == addressBean.getProvince()) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "tips_address_province"));
				} else if (null == addressBean.getCity()) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "tips_address_city"));
				} else if (null == addressBean.getDistrict()) {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "tips_address_district"));
				} else {
					addressBean.setReceiver(consignee);
					addressBean.setDetail(location);
					addressBean.setPostcode(zipcode);
					addressBean.setMobile(telephone);

					String url;
					if (forChange) {
						url = addressBean.getUpdateUrl();
					} else {
						url = Constants.ACCOUNT_ADD_ADDRESS_URL;
					}
					MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
					formData.add("province", addressBean.getProvince());
					formData.add("city", addressBean.getCity());
					formData.add("district", addressBean.getDistrict());
					formData.add("detailaddress", addressBean.getDetail());
					formData.add("postcode", addressBean.getPostcode());
					formData.add("mobile", addressBean.getMobile());
					formData.add("receiver", addressBean.getReceiver());
					new myAddAddressTask(url, formData, mContext).execute();
				}

			}
		});
		mOnEditTextListener.OnEtSet(mConsignee, mLocation, mZipcode, mTel);
		return localView1;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (forChange) {
			mConsignee.setText(addressBean.getReceiver());
			mZipcode.setText(addressBean.getPostcode());
			mLocation.setText(addressBean.getDetail());
			mTel.setText(addressBean.getMobile());
			String proviceString = addressBean.getProvince();
			int proIndex = getKeys(proviceString, provinceMap).get(0);
			ArrayList<Integer> proSubIndex = mAdressList
					.get(Integer.parseInt(mAdressList.get(proIndex)
							.getParentIndex())).getSubIndex();
			for (Integer integer : proSubIndex) {
				if (mAdressList.get(integer).getName().equals(proviceString)) {
					spPro.setSelection(integer.intValue() - 1);
					LogUtil.d(TAG, "spPro.setSelection "
							+ (integer.intValue() - 1));
				}
			}

			String cityString = addressBean.getCity();
			ArrayList<Integer> cityKeys = getKeys(cityString, cityMap);
			int cityIndex = 0;
			for (Integer integer : cityKeys) {
				if ((mAdressList.get(integer.intValue()).getParentIndex())
						.equals(String.valueOf(proIndex))) {
					cityIndex = integer.intValue();
				}
			}
			ArrayList<Integer> citySubIndex = mAdressList.get(proIndex)
					.getSubIndex();
			for (int i = 0; i < citySubIndex.size(); i++) {
				if (citySubIndex.get(i) == cityIndex) {
					spCity.setSelection(i + 1);
				}
			}
			String disString = addressBean.getDistrict();
			ArrayList<Integer> disKeys = getKeys(disString, disMap);
			int disIndex = 0;
			for (Integer integer : disKeys) {
				if (mAdressList.get(integer.intValue()).getParentIndex()
						.equals(String.valueOf(cityIndex))) {
					disIndex = integer.intValue();
				}
			}
			ArrayList<Integer> disSubIndex = mAdressList.get(cityIndex)
					.getSubIndex();
			for (int i = 0; i < disSubIndex.size(); i++) {
				if (disSubIndex.get(i) == disIndex)
					spDistrict.setSelection(i + 1);

			}
		}
	}

	class myAddAddressTask extends ProtectTask {

		public myAddAddressTask(String actionUrl,
				MultiValueMap<String, String> formData, Context mContext) {
			super(actionUrl, formData, mContext);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			butSubmit.setClickable(false);
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
					mOnAddSucListener.OnAddSuc();
				} else {
					ToastUtil.show(mContext, ApplicationUtils.getResId(
							"string", "order_submit_exception_send_data"));
				}
				butSubmit.setClickable(true);
			}
		}
	}

	public class DistrictAdapter extends BaseAdapter {
		AddressInfo parentIndex = null;

		public void setPatentAddr(int index) {
			if (index == 0) {
				parentIndex = null;
			} else {
				parentIndex = mAdressList.get(index);
			}
		}

		public int getCount() {
			if (parentIndex == null) {
				return 1;
			} else {
				return parentIndex.getSubIndex().size() + 1;
			}
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			if (position != 0) {
				zipcodeString = mAdressList.get(
						parentIndex.getSubIndex().get(position - 1))
						.getZipCode();
				/*
				 * if((oldPostion >= -1)&&(oldPostion != position)){
				 * mZipcode.setText(zipcode); if(count == 0){ count = 1; }else{
				 * count = 0; }
				 * 
				 * }else if((oldPostion >= -1)&&(oldPostion == position)){ if
				 * (count < 1){ mZipcode.setText(zipcode); count++; } }
				 * oldPostion = position;
				 */
			}

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(mContext);
			ll.setOrientation(LinearLayout.HORIZONTAL);

			// 初始化TextView
			TextView tv = new TextView(mContext);
			String address = null;
			if (position == 0) {
				address = getResources().getString(
						ApplicationUtils.getResId("string",
								"tips_address_district"));
			} else {
				address = mAdressList.get(
						parentIndex.getSubIndex().get(position - 1)).getName();

				addressBean.setDistrict(address);
			}
			tv.setHeight((int) (getResources().getDimension(ApplicationUtils
					.getResId("dimen", "text_size_normal"))) * 3);
			tv.setText(address);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(
					TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(
							ApplicationUtils.getResId("dimen",
									"text_size_normal")));
			tv.setGravity(Gravity.CENTER_VERTICAL);// 垂直居中
			tv.setPadding((int) (getResources().getDimension(ApplicationUtils
					.getResId("dimen", "input_padding"))), 0, 0, 0);
			ll.addView(tv);
			return ll;
		}
	}

	public class ProvinceAdapter extends BaseAdapter {
		AddressInfo parentIndex = null;

		public void setPatentAddr(int index) {
			if (index == 0) {
				parentIndex = null;
			} else {
				parentIndex = mAdressList.get(index);
			}
		}

		public int getCount() {
			if (parentIndex == null) {
				return 1;
			} else {
				return parentIndex.getSubIndex().size() + 1;
			}
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			if (spCityAdp != null) {
				if (position == 0) {
					spCityAdp.setPatentAddr(0);
				} else {
					spCityAdp.setPatentAddr(parentIndex.getSubIndex().get(
							position - 1));
				}

				spCityAdp.notifyDataSetChanged();
			}
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(mContext);
			ll.setOrientation(LinearLayout.HORIZONTAL);

			// 初始化TextView
			TextView tv = new TextView(mContext);
			String address = null;
			if (position == 0) {
				address = getResources().getString(
						ApplicationUtils.getResId("string",
								"tips_address_province"));
			} else {
				address = mAdressList.get(
						parentIndex.getSubIndex().get(position - 1)).getName();
				addressBean.setProvince(address);
			}
			tv.setHeight((int) (getResources().getDimension(ApplicationUtils
					.getResId("dimen", "text_size_normal"))) * 3);
			tv.setText(address);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(
					TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(
							ApplicationUtils.getResId("dimen",
									"text_size_normal")));
			tv.setGravity(Gravity.CENTER_VERTICAL);// 垂直居中
			tv.setPadding((int) (getResources().getDimension(ApplicationUtils
					.getResId("dimen", "input_padding"))), 0, 0, 0);
			ll.addView(tv);
			return ll;
		}
	}

	public class CityAdapter extends BaseAdapter {
		AddressInfo parentIndex = null;

		public void setPatentAddr(int index) {
			if (index == 0) {
				parentIndex = null;
			} else {
				parentIndex = mAdressList.get(index);
			}
		}

		public int getCount() {
			if (parentIndex == null) {
				return 1;
			} else {
				return parentIndex.getSubIndex().size() + 1;
			}
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			if (spDistrictAdp != null) {
				if (position == 0) {
					spDistrictAdp.setPatentAddr(0);
				} else {
					spDistrictAdp.setPatentAddr(parentIndex.getSubIndex().get(
							position - 1));
				}

				spDistrictAdp.notifyDataSetChanged();
			}

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(mContext);
			ll.setOrientation(LinearLayout.HORIZONTAL);

			// 初始化TextView
			TextView tv = new TextView(mContext);
			String address = null;
			if (position == 0) {
				address = getResources().getString(
						ApplicationUtils
								.getResId("string", "tips_address_city"));
			} else {
				address = mAdressList.get(
						parentIndex.getSubIndex().get(position - 1)).getName();
				addressBean.setCity(address);
			}
			tv.setHeight((int) (getResources().getDimension(ApplicationUtils
					.getResId("dimen", "text_size_normal"))) * 3);
			tv.setText(address);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(
					TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(
							ApplicationUtils.getResId("dimen",
									"text_size_normal")));
			tv.setGravity(Gravity.CENTER_VERTICAL);// 垂直居中
			tv.setPadding((int) (getResources().getDimension(ApplicationUtils
					.getResId("dimen", "input_padding"))), 0, 0, 0);
			ll.addView(tv);
			return ll;
		}
	}

	public void loadAddressInfo() {
		mAdressList = new ArrayList<AddressInfo>();
		BufferedReader br = null;
		String line;
		int i = 0;
		// 索引 上级索引编码 名称 级别 邮政编码
		try {
			InputStream in = getResources().openRawResource(
					ApplicationUtils.getResId("raw", "addr"));
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));// 注意编码

			try {
				line = br.readLine();
				line = br.readLine();

				while ((line = br.readLine()) != null) {
					String str[] = line.split(" ");
					AddressInfo addressInfo = new AddressInfo(str[0], str[1],
							str[2], str[3], str[4]);
					int j = Integer.parseInt(str[0]);
					while (j > mAdressList.size()) {
						AddressInfo addrInfo = new AddressInfo();
						mAdressList.add(addrInfo);
					}
					mAdressList.add(j, addressInfo);

					mAdressList.get(Integer.parseInt(str[1])).addSubIndex(j);

					if (addressInfo.getStep().equals("1")) {
						provinceMap.put(j, addressInfo);
					} else if (addressInfo.getStep().equals("2")) {
						cityMap.put(j, addressInfo);
					} else if (addressInfo.getStep().equals("3")) {
						disMap.put(j, addressInfo);
					}
				}
				br.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public ArrayList<Integer> getKeys(String name,
			HashMap<Integer, AddressInfo> map) {
		ArrayList<Integer> keys = new ArrayList<Integer>();
		for (Entry<Integer, AddressInfo> entry : map.entrySet()) {
			if (name.equals(entry.getValue().getName())) {
				keys.add(entry.getKey());
			} else {
				continue;
			}
		}
		return keys;
	}

}
