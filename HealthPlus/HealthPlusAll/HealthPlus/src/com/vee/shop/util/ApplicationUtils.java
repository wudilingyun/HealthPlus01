package com.vee.shop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Application utilities.
 */
public class ApplicationUtils {

	private static String mAdSweepString = null;

	private static String mRawStartPage = null;
	private static String mRawStartPageStyles = null;
	private static String mRawStartPageBookmarks = null;
	private static String mRawStartPageHistory = null;

	private static String mRawStartPageSearch = null;

	private static int mFaviconSize = -1;
	private static int mImageButtonSize = -1;
	private static int mFaviconSizeForBookmarks = -1;

	private static String mPackageName = null;

	// private static BaseAlertDialog myDialog = null;

	/**
	 * Share a page.
	 * 
	 * @param activity
	 *            The parent activity.
	 * @param title
	 *            The page title.
	 * @param url
	 *            The page url.
	 */
	public static void sharePage(Activity activity, String title, String url) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);

		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, url);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);

		String packagename = activity.getPackageName();

		try {
			activity.startActivity(Intent.createChooser(shareIntent, activity
					.getString(getResId("string",
							"browser_Main_ShareChooserTitle", packagename))));
		} catch (android.content.ActivityNotFoundException ex) {
			// if no app handles it, do nothing
		}
	}

	public static void setPackageName(String packageName) {
		mPackageName = packageName;
	}

	public static int getImageButtonSize(Activity activity) {
		if (mImageButtonSize == -1) {
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

			switch (metrics.densityDpi) {
			case DisplayMetrics.DENSITY_LOW:
				mImageButtonSize = 16;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				mImageButtonSize = 32;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				mImageButtonSize = 48;
				break;
			default:
				mImageButtonSize = 32;
			}
		}

		return mImageButtonSize;
	}

	/**
	 * Get the required size of the favicon, depending on current screen
	 * density.
	 * 
	 * @param activity
	 *            The current activity.
	 * @return The size of the favicon, in pixels.
	 */
	public static int getFaviconSize(Activity activity) {
		if (mFaviconSize == -1) {
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

			switch (metrics.densityDpi) {
			case DisplayMetrics.DENSITY_LOW:
				mFaviconSize = 12;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				mFaviconSize = 24;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				mFaviconSize = 32;
				break;
			default:
				mFaviconSize = 24;
			}
		}

		return mFaviconSize;
	}

	/**
	 * Get the required size of the favicon, depending on current screen
	 * density.
	 * 
	 * @param activity
	 *            The current activity.
	 * @return The size of the favicon, in pixels.
	 */
	public static int getFaviconSizeForBookmarks(Activity activity) {
		if (mFaviconSizeForBookmarks == -1) {
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

			switch (metrics.densityDpi) {
			case DisplayMetrics.DENSITY_LOW:
				mFaviconSizeForBookmarks = 12;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				mFaviconSizeForBookmarks = 16;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				mFaviconSizeForBookmarks = 24;
				break;
			default:
				mFaviconSizeForBookmarks = 16;
			}
		}

		return mFaviconSizeForBookmarks;
	}

	/**
	 * Display a standard yes / no dialog.
	 * 
	 * @param context
	 *            The current context.
	 * @param icon
	 *            The dialog icon.
	 * @param title
	 *            The dialog title.
	 * @param message
	 *            The dialog message.
	 * @param onYes
	 *            The dialog listener for the yes button.
	 */
	public static void showYesNoDialog(Context context, int icon, int title,
			int message, DialogInterface.OnClickListener onYes) {
		String packagename = context.getPackageName();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(icon);
		builder.setTitle(context.getResources().getString(title));
		builder.setMessage(context.getResources().getString(message));

		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton(
				getResId("string", "browser_Commons_Yes", packagename), onYes);
		builder.setNegativeButton(
				getResId("string", "browser_Commons_No", packagename),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Display a continue / cancel dialog.
	 * 
	 * @param context
	 *            The current context.
	 * @param icon
	 *            The dialog icon.
	 * @param title
	 *            The dialog title.
	 * @param message
	 *            The dialog message.
	 * @param onContinue
	 *            The dialog listener for the continue button.
	 * @param onCancel
	 *            The dialog listener for the cancel button.
	 */
	public static void showContinueCancelDialog(Context context, int icon,
			String title, String message,
			DialogInterface.OnClickListener onContinue,
			DialogInterface.OnClickListener onCancel) {
		String packagename = context.getPackageName();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setIcon(icon);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton(
				getResId("string", "browser_Commons_Continue", packagename),
				onContinue);
		builder.setNegativeButton(
				getResId("string", "browser_Commons_Cancel", packagename),
				onCancel);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Display a standard Ok dialog.
	 * 
	 * @param context
	 *            The current context.
	 * @param icon
	 *            The dialog icon.
	 * @param title
	 *            The dialog title.
	 * @param message
	 *            The dialog message.
	 */
	public static void showOkDialog(Context context, int icon, String title,
			String message) {
		String packagename = context.getPackageName();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setIcon(icon);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton(
				getResId("string", "browser_Commons_Ok", packagename),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Display a standard Ok / Cancel dialog.
	 * 
	 * @param context
	 *            The current context.
	 * @param icon
	 *            The dialog icon.
	 * @param title
	 *            The dialog title.
	 * @param message
	 *            The dialog message.
	 * @param onYes
	 *            The dialog listener for the yes button.
	 */
	public static void showOkCancelDialog(Context context, int icon,
			String title, String message, DialogInterface.OnClickListener onYes) {
		String packagename = context.getPackageName();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		builder.setIcon(icon);
		builder.setTitle(title);
		builder.setMessage(message);

		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton(
				getResId("string", "browser_Commons_Ok", packagename), onYes);
		builder.setNegativeButton(
				getResId("string", "browser_Commons_Cancel", packagename),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Check if the SD card is available. Display an alert if not.
	 * 
	 * @param context
	 *            The current context.
	 * @param showMessage
	 *            If true, will display a message for the user.
	 * @return True if the SD card is available, false otherwise.
	 */
	public static boolean checkCardState(Context context, boolean showMessage) {
		String packagename = context.getPackageName();
		// Check to see if we have an SDCard
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {

			int messageId;

			// Check to see if the SDCard is busy, same as the music app
			if (status.equals(Environment.MEDIA_SHARED)) {
				messageId = getResId("string",
						"browser_Commons_SDCardErrorSDUnavailable", packagename);
			} else {
				messageId = getResId("string",
						"browser_Commons_SDCardErrorNoSDMsg", packagename);
			}

			if (showMessage) {
				ApplicationUtils.showErrorDialog(
						context,
						getResId("string", "browser_Commons_SDCardErrorTitle",
								packagename), messageId);
			}

			return false;
		}

		return true;
	}

	/**
	 * Show an error dialog.
	 * 
	 * @param context
	 *            The current context.
	 * @param title
	 *            The title string id.
	 * @param message
	 *            The message string id.
	 */
	public static void showErrorDialog(Context context, int title, int message) {
		String packagename = context.getPackageName();
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(message)
				.setPositiveButton(
						getResId("string", "browser_Commons_Ok", packagename),
						null).show();
	}

	public static void showErrorDialog(Context context, int title,
			String message) {
		String packagename = context.getPackageName();
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(message)
				.setPositiveButton(
						getResId("string", "browser_Commons_Ok", packagename),
						null).show();
	}

	/**
	 * Load a raw string resource.
	 * 
	 * @param context
	 *            The current context.
	 * @param resourceId
	 *            The resource id.
	 * @return The loaded string.
	 */
	private static String getStringFromRawResource(Context context,
			int resourceId) {
		String result = null;

		InputStream is = context.getResources().openRawResource(resourceId);
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} catch (IOException e) {
				Log.w("ApplicationUtils", String.format(
						"Unable to load resource %s: %s", resourceId,
						e.getMessage()));
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					Log.w("ApplicationUtils", String.format(
							"Unable to load resource %s: %s", resourceId,
							e.getMessage()));
				}
			}
			result = sb.toString();
		} else {
			result = "";
		}

		return result;
	}

	/**
	 * Load the AdSweep script if necessary.
	 * 
	 * @param context
	 *            The current context.
	 * @return The AdSweep script.
	 */
	public static String getAdSweepString(Context context) {
		String packagename = context.getPackageName();
		if (mAdSweepString == null) {
			InputStream is = context.getResources().openRawResource(
					getResId("raw", "browser_adsweep", packagename));
			if (is != null) {
				StringBuilder sb = new StringBuilder();
				String line;

				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "UTF-8"));
					while ((line = reader.readLine()) != null) {
						if ((line.length() > 0) && (!line.startsWith("//"))) {
							sb.append(line).append("\n");
						}
					}
				} catch (IOException e) {
					Log.w("AdSweep",
							"Unable to load AdSweep: " + e.getMessage());
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						Log.w("AdSweep",
								"Unable to load AdSweep: " + e.getMessage());
					}
				}
				mAdSweepString = sb.toString();
			} else {
				mAdSweepString = "";
			}
		}
		return mAdSweepString;
	}

	/**
	 * Load the changelog string.
	 * 
	 * @param context
	 *            The current context.
	 * @return The changelog string.
	 */
	public static String getChangelogString(Context context) {
		String packagename = context.getPackageName();
		return getStringFromRawResource(context,
				getResId("raw", "browser_changelog", packagename));
	}

	/**
	 * Get the application version code.
	 * 
	 * @param context
	 *            The current context.
	 * @return The application version code.
	 */
	public static int getApplicationVersionCode(Context context) {

		int result = -1;

		try {

			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);

			result = info.versionCode;

		} catch (NameNotFoundException e) {
			Log.w("ApplicationUtils",
					"Unable to get application version: " + e.getMessage());
			result = -1;
		}

		return result;
	}

	/**
	 * Copy a text to the clipboard.
	 * 
	 * @param context
	 *            The current context.
	 * @param text
	 *            The text to copy.
	 * @param toastMessage
	 *            The message to show in a Toast notification. If empty or null,
	 *            does not display notification.
	 */
	public static void copyTextToClipboard(Context context, String text,
			String toastMessage) {
		ClipboardManager clipboard = (ClipboardManager) context
				.getSystemService(Activity.CLIPBOARD_SERVICE);
		clipboard.setText(text);

		if ((toastMessage != null) && (toastMessage.length() > 0)) {
			Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
		}
	}

	public static Integer getResId(String rType, String rName,
			String packagename) {
		Object localObject = null;
		;
		try {
			Class localClass = Class.forName(packagename + ".R" + "$" + rType);
			Field localField = localClass.getField(rName);
			localObject = localField.get(localClass.newInstance());
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return Integer.valueOf(Integer.parseInt(localObject.toString()));
	}

	public static Integer getResId(String rType, String rName) {
		return getResId(rType, rName, mPackageName);
	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public static boolean isAppOnForeground(Context mContext) {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) mContext
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		String packageName = mContext.getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	// public static BaseAlertDialog getMyDialog() {
	// return myDialog;
	// }
	//
	// public static void showAlertDialog(Context context, int icon, int title,
	// int message, int pos, int neg, View.OnClickListener onYes,
	// View.OnClickListener onNo) {
	// myDialog = new BaseAlertDialog(context);
	// myDialog.setCancelable(false);
	// if (-1 != icon) {
	// myDialog.setIcon(icon);
	// }
	// if (-1 != title) {
	// myDialog.setTitle(context.getResources().getString(title));
	// }
	// if (-1 != message) {
	// myDialog.setMessage(context.getResources().getString(message));
	// }
	// myDialog.setPositiveButton(pos, onYes);
	// myDialog.setNegativeButton(neg, onNo);
	// myDialog.show();
	// }

	/*
	 * public static void showLogDialog(Context context, View.OnClickListener
	 * onYes, View.OnClickListener onNo) { showAlertDialog(context, -1,
	 * getResId("string", "shop_log_dialog_title"), getResId("string",
	 * "shop_log_dialog_message"), getResId("string", "Ensure"),
	 * getResId("string", "Cancel"), onYes, onNo); }
	 */
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
