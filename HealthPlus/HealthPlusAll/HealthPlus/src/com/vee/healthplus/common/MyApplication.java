package com.vee.healthplus.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.vee.healthplus.R;
import com.vee.healthplus.ui.main.FirstActivity;
import com.vee.healthplus.ui.setting.SettingShare;
import com.vee.healthplus.util.ImageLoader;
import com.vee.shop.bean.CartItemBean;
import com.vee.shop.util.ApplicationUtils;

public class MyApplication extends Application implements TagAliasCallback  {
	public static final String TAG = "xuxuxu";

	private static MyApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;
	// public LocationClient mLocationClient = null;
	// public MyLocationListenner myListener = new MyLocationListenner();
	//public static final String CHANNEL_ID = "700071";

	public static final String strKey = "zigTyzIrZBCivDexGNGNGDG7";

	private static Typeface face = null;
	
	/*
	 * 注意：为了给用户提供更安全的服务，Android SDK自v2.1.3版本开始采用了全新的Key验证体系。
	 * 因此，当您选择使用v2.1.3及之后版本的SDK时，需要到新的Key申请页面进行全新Key的申请， 申请及配置流程请参考开发指南的对应章节
	 */

	public static int cartNum;
	public static HashMap<String, CartItemBean> cartMap;
	 Set<String> tags = new HashSet();
	 
		private Bitmap mDefaultAvatar;
		public static List<String> mEmoticons = new ArrayList<String>();
		public static Map<String, Integer> mEmoticonsId = new HashMap<String, Integer>();
		public static List<String> mEmoticons_Zem = new ArrayList<String>();
		public static List<String> mEmoticons_Zemoji = new ArrayList<String>();
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		// if (!isMyServiceRunning(this))
		initEngineManager(this);
		try {
			face = Typeface.createFromAsset(getAssets(), "fonts/minmin.ttf");
		} catch (Exception e) {

		}
		ApplicationUtils.setPackageName(getPackageName());
		JPushInterface.setDebugMode(true);
         JPushInterface.init(this);
         
         //表情
         
	}
	
	void  getIcon(){
		mDefaultAvatar = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_common_def_header);
		for (int i = 1; i < 64; i++) {
			String emoticonsName = "[zem" + i + "]";
			int emoticonsId = getResources().getIdentifier("zem" + i,
					"drawable", getPackageName());
			mEmoticons.add(emoticonsName);
			mEmoticons_Zem.add(emoticonsName);
			mEmoticonsId.put(emoticonsName, emoticonsId);
		}
		for (int i = 1; i < 59; i++) {
			String emoticonsName = "[zemoji" + i + "]";
			int emoticonsId = getResources().getIdentifier("zemoji_e" + i,
					"drawable", getPackageName());
			mEmoticons.add(emoticonsName);
			mEmoticons_Zemoji.add(emoticonsName);
			mEmoticonsId.put(emoticonsName, emoticonsId);
		}

	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			/*
			 * Toast.makeText(MyApplication.getInstance().getApplicationContext()
			 * , "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
			 */
		}

		// mLocationClient = new LocationClient( this );
		// /**——————————————————————————————————————————————————————————————————
		// * 这里的AK和应用签名包名绑定，如果使用在自己的工程中需要替换为自己申请的Key
		// * ——————————————————————————————————————————————————————————————————
		// */
		// mLocationClient.setAK(strKey);
		// mLocationClient.registerLocationListener( myListener );
	}

	public static MyApplication getInstance() {
		return mInstance;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				// Toast.makeText(MyApplication.getInstance().getApplicationContext(),
				// "您的网络出错啦！",
				// Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				// Toast.makeText(MyApplication.getInstance().getApplicationContext(),
				// "输入正确的检索条件！",
				// Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				// Toast.makeText(MyApplication.getInstance().getApplicationContext(),
				// "请在 DemoApplication.java文件输入正确的授权Key！",
				// Toast.LENGTH_LONG).show();
				MyApplication.getInstance().m_bKeyRight = false;
			}
		}
	}

	public static void shareBySystem(Context mContext, String sendMsg,
			String imgUrl, String url, String id, String type,String brief) {
		String imgPath = "";

		try {
			File imgFile = ImageLoader.getFileByName(ImageLoader
					.getFileNameByUrl(imgUrl));

			if (imgFile != null && imgFile.exists() && imgFile.canRead()
					&& imgFile.length() > 0) {
				imgPath = imgFile.getAbsolutePath();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.setClass(mContext, SettingShare.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("content", sendMsg);
		intent.putExtra("imgPath", imgUrl);
		intent.putExtra("url", url);
		intent.putExtra("id", id);
		intent.putExtra("type", type);
		intent.putExtra("brief", brief);
		mContext.startActivity(intent);
	}

	public static void createMyGameShortCut(Context context) {
		// delShortcut(context);

		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
				.getString(R.string.hp_app_name));
		shortcut.putExtra("duplicate", false); // 不允许重复创建

		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.setClassName(context, FirstActivity.class.getName());
		shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

		// 快捷方式的图标
		Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource
				.fromContext(context, R.drawable.ic_launcher);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		context.sendBroadcast(shortcut);

	}

	private static void delShortcut(Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
				.getString(R.string.hp_app_name));

		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
		Log.e("xuxuxu", FirstActivity.class.getName());
		String appClass = FirstActivity.class.getName();// "com.vee.healthplus.ui.main.LoginMain";
		ComponentName comp = new ComponentName(context.getPackageName(),
				appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));

		context.sendBroadcast(shortcut);

	}

	public static String getRealApkName(String name) {
		if (name.contains("apps")) {
			return name.substring(5, name.length());
		} else
			return name;
	}

	public static boolean startInstall(String pkname, Context context) {
		try {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ Common.DOWNLOADDIRNAME;
			String strTmp = getRealApkName(pkname);
			File file = new File(path, strTmp);
			if (file != null && file.exists() && file.length() > 0) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				context.startActivity(intent);
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isMyServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			// Log.i("xuxuxu",
			// service.service.getClassName()+":"+service.service.getPackageName());
			if ("com.vee.healthplus.service.SportEngine".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static Typeface getTypeFace() {
		return face;
	}

	public static HashMap<String, CartItemBean> getCartMap() {
		return cartMap;
	}

	public static void setCartMap(HashMap<String, CartItemBean> cartMap) {
		MyApplication.cartMap = cartMap;
	}

	public static int getCartNum() {
		return cartNum;
	}

	public static void setCartNum(int cartNum) {
		MyApplication.cartNum = cartNum;
	}

	@Override
	public void gotResult(int arg0, String arg1, Set<String> arg2) {
		// TODO Auto-generated method stub
		
	}
}