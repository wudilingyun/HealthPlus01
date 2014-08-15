package com.vee.healthplus.util;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.method.DialerKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.widget.CustomToast;

public class SystemMethod {

    public static String getString(Context context, int path) {
        return context.getResources().getString(path);
    }

    public static String getString(Context context, int path, int value) {
        return String.format(getString(context, path), value);
    }

    public static String getString(Context context, int path, String value) {
        return String.format(getString(context, path), value);
    }

    public static String[] getStringArray(Context context, int path) {
        return context.getResources().getStringArray(path);
    }

    public static void showShortToast(String text, Context context) {
        CustomToast tosToast = new CustomToast(context);
        tosToast.makeCustomText(context, text, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(int path, Context context) {
        String text = getString(context, path);
        CustomToast tosToast = new CustomToast(context);
        tosToast.makeCustomText(context, text, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(Context context, int path) {
        String text = getString(context, path);
        CustomToast tosToast = new CustomToast(context);
        tosToast.makeCustomText(context, text, Toast.LENGTH_SHORT);
    }

    public static void showShortToast(int path, String value, Context context) {
        showShortToast(getString(context, path, value), context);
    }

    public static void showShortToast(int path, int value, Context context) {
        showShortToast(getString(context, path, value), context);
    }


    public static int getResId(Context mContext, String resName, String type) {
        return mContext.getResources().getIdentifier(resName, type,
                mContext.getPackageName());
    }


    public static boolean isNetAvailable(Context context) {
        boolean b = true;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            // showShortToast(R.string.promotion_no_net_warn, context);
            b = false;
        }

        return b;
    }

    public static boolean isNetAvailableNotips(Context context) {
        boolean b = true;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            b = false;
        }

        return b;
    }


    public static void setFonts(ViewGroup root, Context context) {  
    	   
//        Typeface tf = Typeface.createFromAsset(context.getAssets(),  
//               "fonts/MSYH.TTF");  
//    
//        for (int i = 0; i < root.getChildCount(); i++) {  
//            View v = root.getChildAt(i);  
//            if (v instanceof TextView) {  
//               ((TextView) v).setTypeface(tf);  
//            } else if (v instanceof Button) {  
//               ((Button) v).setTypeface(tf);  
//            } else if (v instanceof EditText) {  
//               ((EditText) v).setTypeface(tf);  
//            } else if (v instanceof ViewGroup) {  
//            	setFonts((ViewGroup) v, context);  
//            }  
//        }  
     }  
    
    public static void setFontsYH(TextView view, Context context) {  
        Typeface tf = Typeface.createFromAsset(context.getAssets(),  
               "fonts/MSYH.TTF");  
        view.setTypeface(tf);  
     } 
    
    public static void setFontsRB(TextView view, Context context) {  
        Typeface tf = Typeface.createFromAsset(context.getAssets(),  
               "fonts/Roboto-Condensed.ttf");  
        view.setTypeface(tf);  
     } 
    
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static boolean isSDAvailable(Context context) {
        boolean b = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (!b) {
            //showShortToast(R.string.appmgr_update_nosd, context);
        }
        return b;
    }

    public static void setKeyboard(EditText editText) {
        // editText.setInputType(InputType.TYPE_CLASS_NUMBER
        // | InputType.TYPE_NUMBER_FLAG_SIGNED
        // | InputType.TYPE_TEXT_VARIATION_PASSWORD
        // |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        editText.setKeyListener(DialerKeyListener.getInstance());
    }
}
