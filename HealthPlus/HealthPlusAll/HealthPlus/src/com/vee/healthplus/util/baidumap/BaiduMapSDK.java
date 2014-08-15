package com.vee.healthplus.util.baidumap;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.vee.healthplus.common.MyApplication;

public class BaiduMapSDK {
    public static final String TAG = "xuxuxu";
    MyApplication app;
    private static BaiduMapSDK mInstance = null;
    public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;
    private static Context context;

    public static final String strKey = "7ad43fd86ee1c6b0404cb4f5e9877096";
    /*
        注意：为了给用户提供更安全的服务，Android SDK自v2.1.3版本开始采用了全新的Key验证体系。
    	因此，当您选择使用v2.1.3及之后版本的SDK时，需要到新的Key申请页面进行全新Key的申请，
    	申请及配置流程请参考开发指南的对应章节
    */

    private BaiduMapSDK(Context ctx) {
        // TODO Auto-generated constructor stub
        mInstance = this;
        this.context = ctx;
        app = MyApplication.getInstance();
        mBMapManager = app.mBMapManager;
//		initEngineManager();
    }

    public void initEngineManager() {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey, new MyGeneralListener())) {
            Toast.makeText(context,
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }

//        mLocationClient = new LocationClient( this );
//		/**——————————————————————————————————————————————————————————————————
//		 * 这里的AK和应用签名包名绑定，如果使用在自己的工程中需要替换为自己申请的Key
//		 * ——————————————————————————————————————————————————————————————————
//		 */
//		mLocationClient.setAK(strKey);
//		mLocationClient.registerLocationListener( myListener );
    }

    public static BaiduMapSDK getInstance(Context ctx) {
        if (mInstance != null)
            return mInstance;
        else {
            return new BaiduMapSDK(ctx);
        }
    }


    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(context, "您的网络出错啦！",
                        Toast.LENGTH_LONG).show();
            } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(context, "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
                Toast.makeText(context,
                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                mInstance.m_bKeyRight = false;
            }
        }
    }
}