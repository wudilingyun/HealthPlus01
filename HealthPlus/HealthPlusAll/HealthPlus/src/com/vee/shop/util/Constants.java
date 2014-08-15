/**
 * 
 */
package com.vee.shop.util;

import android.os.Environment;

/**
 * @author Felix
 * 
 */
public class Constants {

	/**
	 * 云狐专区的地址
	 */
	public static final String FOXCENTER_URL = "http://zhuanqu.mobifox.cn/release/mobile/yunhuzhuanqu/index.html?tn=sjscs";
	public static final String USER_FROM = "shop";

	public static final String ALLSYSTEMSETTING_PREFERENCES = "HealthPlusSystemSetting";

	public static final String EXTERNAL_STORAGE_PATH = Environment
			.getExternalStorageDirectory().getPath();

	public static final int PRIVATE_RESULT_CODE_OK = 1;
	public static final int PRIVATE_RESULT_CODE_CANCEL = -1;

	public static final String PAY_TYPE_FACE = "1";
	public static final String PAY_TYPE_ONLINE = "2";
	public static final String DELIVER_TIME_TYPE_NOLIMIT = "1";
	public static final String DELIVER_TIME_TYPE_WORKTIME = "2";
	public static final String DELIVER_TIME_TYPE_HOLIDAY = "3";
	public static final String INVOICE_TYPE_NO = "1";
	public static final String INVOICE_TYPE_PRESON = "2";
	public static final String INVOICE_TYPE_GROUP = "3";

	public static final String ORDER_STATUS_NOPAY = "1";
	public static final String ORDER_STATUS_WAIT_FOT_SEND = "2";
	public static final String ORDER_STATUS_WAIT_FOT_RECEIVE = "3";
	public static final String ORDER_TYPE_DONE = "4";
	public static final String ORDER_TYPE_UNDONE = "5";
	public static final String ORDER_TYPE_ALL = "6";

	public static final int CHECKOU_REQUESTCODE = 100;
	public static final int PAYMENT_REQUESTCODE = 200;
	public static final String PRE_USER_AGENT = "Mozilla/5.0 (Windows NT 5.1; rv:21.0) Gecko/20100101 Firefox/21.0 ";

	public static final int HTTP_TYPE_GET = 1;
	public static final int HTTP_TYPE_POST = 2;

	public static final int PASSWORD_MAX_LENGTH = 16;
	public static final int PASSWORD_MIN_LENGTH = 4;
	// public static final String SERVER_URL =
	// "http://eshop.17fox.cn:11080/mobilemall/";
	// // public static final String SERVER_URL = "192.168.3.159:8080/yhsjsc/";
	// // public static final String SERVER_URL =
	// "http://118.186.34.117:11080/mobilemall";
	// // public static final String SERVER_URL =
	// "http://192.168.3.159:8080/mobilemall";
	// public static final String REQUEST_HOT_LIST_URL = SERVER_URL+"hotest";
	// public static final String REQUEST_CATEGORY_LIST_URL =
	// SERVER_URL+"procductcategory";
	// public static final String ADD_DEL_CART_URL = SERVER_URL+"cart/" ;
	// public static final String QUERY_CART_URL =SERVER_URL+"cart/get" ;
	// public static final String QUERY_ADDRESS_URL
	// =SERVER_URL+"cart/getaddress";
	// public static final String ADD_ADDRESS_URL =
	// SERVER_URL+"cart/addaddress";
	// public static final String CHECKOUT_URL = SERVER_URL+"cart/checkout";
	// public static final String ORDER_COMMIT_URL = SERVER_URL+"cart/order";
	// public static final String ORDERLIST_QUERY_URL =
	// SERVER_URL+"cart/orderlist";
	// public static final String QUERY_SERVICESTATION_URL =
	// SERVER_URL+"servicenetwork/get";

	public static final String CLIENT_ID = "e9fbccdae98d5696";
	public static final String CLIENT_SECRET = "9fa283e1eca2d4e8";
	public static final String CONNECTION_RESPOSITORY_ENCRYPTION_PASSWORD = "9fa283e1eca2d4e8";
	public static final String CONNECTION_RESPOSITORY_ENCRYPTION_SALT = "5c0744940b5c369b";
	public static final String ACCOUNT_BASE_URL = "http://hp.mobifox.cn:12080/healthplus/";
	public static final String ACCOUNT_REGISTER_URL = ACCOUNT_BASE_URL
			+ "android/register";
	public static final String ACCOUNT_LOGIN_URL = ACCOUNT_BASE_URL
			+ "oauth/token";
	public static final String ACCOUNT_REQUEST_HOT_LIST_URL = ACCOUNT_BASE_URL
			+ "hotest";
	public static final String ACCOUNT_REQUEST_ALL_PRODUCT = ACCOUNT_BASE_URL
			+ "client/getallproduct";
	public static final String ACCOUNT_REQUEST_CATEGORY_LIST_URL = ACCOUNT_BASE_URL
			+ "procductcategory";
	public static final String ACCOUNT_ADD_DEL_CART_URL = ACCOUNT_BASE_URL
			+ "cart/";
	public static final String ACCOUNT_QUERY_CART_URL = ACCOUNT_BASE_URL
			+ "cart/get";
	public static final String ACCOUNT_QUERY_ADDRESS_URL = ACCOUNT_BASE_URL
			+ "cart/getaddress";
	public static final String ACCOUNT_ADD_ADDRESS_URL = ACCOUNT_BASE_URL
			+ "cart/addaddress";
	public static final String ACCOUNT_CHECKOUT_URL = ACCOUNT_BASE_URL
			+ "cart/checkout";
	public static final String ACCOUNT_ORDER_COMMIT_URL = ACCOUNT_BASE_URL
			+ "cart/order";
	public static final String ACCOUNT_ORDERLIST_QUERY_URL = ACCOUNT_BASE_URL
			+ "cart/orderlist";
	public static final String ACCOUNT_QUERY_SERVICESTATION_URL = ACCOUNT_BASE_URL
			+ "servicenetwork/get";
	public static final String ACCOUNT_RESET_PWD_URL = ACCOUNT_BASE_URL
			+ "reset";
	public static final String ACCOUNT_POINT_QUERY_URL = ACCOUNT_BASE_URL
			+ "client/score";
	/*
	 * public static final String PAIKE_REGISTER_URL =
	 * "http://124.205.66.52:18080/paike/user/modinfo";
	 * 
	 * // 获取用户id的地址 public static final String QUICKLY_REGISTER_URL =
	 * "http://124.205.66.52/UserCenter/quickregister.php"; // 注册的地址 public
	 * static final String REGISTER_URL =
	 * "http://124.205.66.52/UserCenter/saveuserinfo.php"; // 登录的地址 public
	 * static final String LOGIN_URL =
	 * "http://124.205.66.52/UserCenter/login.php"; // 系统消息 public static final
	 * String SYSINFO_URL =
	 * "http://124.205.66.52/UserCenterEX/upload/uc_announce_test.php"; //
	 * 获取用户信息 public static final String GETUSER_URL =
	 * "http://124.205.66.52/UserCenter/getUserInfo.php"; // 修改密码 public static
	 * final String CHANGEPASS_URL =
	 * "http://124.205.66.52/UserCenter/changePassWord.php"; // 修改昵称，修改邮箱，修改性别
	 * public static final String CHANGEINFO_URL =
	 * "http://124.205.66.52/UserCenter/changeInfo.php"; // 积分说明 public static
	 * final String ACTIVITY_URL =
	 * "http://124.205.66.52/UserCenterEX/upload/getprizerule.php"; // 获奖动态
	 * public static final String GETPRIZE_PERSON_LIST_URL =
	 * "http://124.205.66.52/UserCenterEX/upload/getlastprizelist.php"; // 积分抽奖
	 * public static final String TOGETPRIZE =
	 * "http://124.205.66.52/UserCenterEX/upload/getprize.php"; // 奖品列表 public
	 * static final String PRIZE_LIST =
	 * "http://124.205.66.52/UserCenterEX/upload/prizelistdisplay.php"; // 活动列表
	 * public static final String ACTIVITYLIST_URL =
	 * "http://124.205.66.52/UserCenterEX/upload/activitylistdisplay.php"; //
	 * 图片跟路径 public static final String IMAGE_ROOT =
	 * "http://124.205.66.52/UserCenterEX/upload/"; // 活动图片 public static final
	 * String ACTIVITY_IMAGES =
	 * "http://124.205.66.52/UserCenterEX/upload/getactivitypic.php"; // 活动详细信息
	 * public static final String ACTIVITY_DETAIL =
	 * "http://124.205.66.52/UserCenterEX/upload/activitydisplay.php"; // 参加活动
	 * public static final String ACTIVITY_JOIN =
	 * "http://124.205.66.52/UserCenterEX/upload/joinactivity.php";
	 */

	public static final boolean isImgMemCache = false;
	public static final boolean isImgFileCache = true;
}
