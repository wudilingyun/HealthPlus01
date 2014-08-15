package com.vee.healthplus.util.sporttrack.weather;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.vee.healthplus.util.baidumap.BaiduMapSDK;

public class BMapCity {
	private static BMapCity mInstance = null;

	private MKSearch mSearch = null;
	private Context context;
	private Handler handler;
	private int tag;

	BMapCity(Context context,Handler handler) {
		this.context = context;
		this.handler=handler;
		initSearch();

	}

	private void initSearch() {
		// 初始化搜索模块，注册事件监听
		mSearch = new MKSearch();
		mSearch.init(BaiduMapSDK.getInstance(context).mBMapManager,
				new MKSearchListener() {

					@Override
					public void onGetPoiDetailSearchResult(int type, int error) {
					}

					public void onGetPoiResult(MKPoiResult res, int type,
							int error) {

					}

					public void onGetDrivingRouteResult(
							MKDrivingRouteResult res, int error) {
					}

					public void onGetTransitRouteResult(
							MKTransitRouteResult res, int error) {
					}

					public void onGetWalkingRouteResult(
							MKWalkingRouteResult res, int error) {
					}

					public void onGetAddrResult(MKAddrInfo result, int error) {
						MKGeocoderAddressComponent kk = result.addressComponents;
						String city = kk.city;
						if(city.equals("")) return;
						Message msg=handler.obtainMessage();
						msg.arg1=tag;
						Bundle bundle=new Bundle();
						bundle.putString("city", city);
						msg.setData(bundle);
						handler.sendMessage(msg);
					}

					public void onGetBusDetailResult(MKBusLineResult result,
							int iError) {

					}

					@Override
					public void onGetSuggestionResult(MKSuggestionResult res,
							int arg1) {
					}

					@Override
					public void onGetShareUrlResult(MKShareUrlResult result,
							int type, int error) {
						// TODO Auto-generated method stub

					}

				});
	}

	public void getCity(String latitude, String longitude,int tag) {
		this.tag=tag;
		mSearch.reverseGeocode(new GeoPoint(
				(int) (Double.valueOf(latitude) * 1e6), (int) (Double
						.valueOf(longitude) * 1e6)));
		//test
				Message msg=handler.obtainMessage();
				msg.arg1=tag;
				Bundle bundle=new Bundle();
				bundle.putString("city", "北京");
				msg.setData(bundle);
				handler.sendMessage(msg);
	}
	
	public void getCity(BDLocation location) {
		this.tag=tag;
		mSearch.reverseGeocode(new GeoPoint((int)(location.getLatitude()*1e6),(int)(location.getLongitude()*1e6)));
	}

	public static BMapCity getInstance(Context ctx,Handler handler) {
		//if (mInstance != null)
		//	return mInstance;
		//else {
			return new BMapCity(ctx,handler);
		//}
	}

}
