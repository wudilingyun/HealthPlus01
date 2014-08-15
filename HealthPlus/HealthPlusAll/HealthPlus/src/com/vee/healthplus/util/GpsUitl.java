package com.vee.healthplus.util;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.renderscript.Element;
import android.view.LayoutInflater;
import android.view.View;

import com.vee.healthplus.R;
import com.vee.healthplus.util.sporttrack.TrackEntity;
import com.vee.healthplus.widget.CustomDialog;

public class GpsUitl {
    public static boolean isGpsEnable(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void isGpsEnableWithUI(final Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        
        return;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.hp_gps_warn, null);
        

        CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);
        customBuilder
                .setTitle("注意")
                .setContentView(layout)
                .setPositiveButton("现在设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= 11) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(
                                    Settings.ACTION_SECURITY_SETTINGS);
                            context.startActivity(intent);

                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        Dialog dialog = customBuilder.create();
        dialog.show();
    } 
   
    
    
    public static String checkNull(Object object) {
		if (object == null)
			return "0";
		else
			return (String) object;
	}
    
    //大卡
    public static String caloryFormat(String doubleStr, boolean iswithUnit){ 
    	StringBuffer buffer=new StringBuffer();
    	if (doubleStr == null || doubleStr.equals("")|| Double.parseDouble(doubleStr)==0)
			buffer.append("0");
    	else{ 
    		buffer.append(String.valueOf(doubleStr));
    	}
    	
		return addUnit(iswithUnit,buffer,"卡路里");
    }
    
    //公里
	public static String distanceFormat(String doubleStr, boolean iswithUnit,Context context) {//米--公里
		StringBuffer buffer = new StringBuffer();
		if (doubleStr == null || doubleStr.equals("")|| Double.parseDouble(doubleStr)==0)
			buffer.append("0.00");
		else {
			if(true){
			double doubleValue=(Math.round(Double.parseDouble(doubleStr)/1000*100)/100.0);
			buffer.append(String.valueOf(doubleValue));
			}
			else 	buffer.append((double) (Math.round(Double.parseDouble(doubleStr)/1000*100)/100.0));
		}
		return addUnit(iswithUnit,buffer,"公里");
	}
	
	private static String addUnit(boolean iswithUnit,StringBuffer buffer,String unitStr){
		if (iswithUnit)
			return buffer.append(unitStr).toString();
		else
		return buffer.toString();
	}
    
    //时分秒
    public static String durationTrackFormat(String longStr){
    	StringBuffer buffer=new StringBuffer();
    	 if (longStr == null || longStr.equals(""))
    		 buffer.append("0'0''") ;
    	 else{
    		 long longValue=Long.parseLong(longStr);
    		 if(longValue/ (1000*60*60)>1){
    			 buffer.append(longValue / (1000) / 60/60 + "°");
    			 buffer.append(longValue / (1000) / 60% 60 + "'").append(longValue / (1000) % 60 + "''");
    		 }
    		 else  buffer.append(longValue / (1000) / 60 + "'").append(longValue / (1000) % 60 + "''");

    	 }
    	return buffer.toString();
    }
    
    //时分秒
    public static String countFormat(String str,boolean iswithUnit){
    	StringBuffer buffer=new StringBuffer();
    	 if (str == null || str.equals(""))
    		 buffer.append("0") ;
    	 else{
    		buffer.append(str);
    	 }
    	 return addUnit(iswithUnit,buffer,"次");
    }
    
    public static String velocityFormat(String str,boolean iswithUnit){//米/小时--公里/小时
    	StringBuffer buffer=new StringBuffer();
    	 if (str == null || str.equals(""))
    		 buffer.append("0") ;
    	 else{
 			double doubleValue=(Math.round(Double.parseDouble(str)/1000*100)/100.0);
 			buffer.append(String.valueOf(doubleValue));
    	 }
    	 return addUnit(iswithUnit,buffer,"公里/小时");
    }
    
    public static String formatToString(long milli) {
        int hours = (int) (milli / 3600000);
        int minutes = (int) (milli % 3600000) / 60000;
        int seconds = (int) ((milli % 3600000) % 60000 / 1000);
        StringBuilder time = new StringBuilder();
        if (hours > 0) {
            if (hours < 10)
                time.append("0" + hours+"h");
            else
                time.append(hours);
        }
        if (minutes > 0) {
            if (minutes < 10)
                time.append("0" + minutes+"'");
            else
                time.append(minutes);
        }
        if (seconds > 0) {
            if (seconds < 10)
                time.append("0" + seconds);
            else
                time.append(seconds);
        }
        return time.toString();
    }
}
