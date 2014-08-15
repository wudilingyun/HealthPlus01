package com.vee.shop.alipay.net;

/**
 * 自定义异常
 * 
 * @author LY
 * 
 */
public class MyException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 未定义的错误码
	 */
	public static final int ERRORCODE_UNKNOW_CODE = 900;
	/**
	 * 错误的用户名
	 */
	public static final int ERRORCODE_ERROR_USERNAME = 800;

	public int errorCode;
	public String errorMsg;

	public MyException(int errorCode, String msg) {
		super(msg);
		this.errorMsg = msg;
		this.errorCode = errorCode;
	}
}
