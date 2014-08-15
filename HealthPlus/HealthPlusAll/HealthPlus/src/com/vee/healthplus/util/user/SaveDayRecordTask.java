package com.vee.healthplus.util.user;

import android.app.Activity;
import android.os.AsyncTask;

import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.ResourceAccessException;

/**
 * Created by wangjiafeng on 13-11-20.
 */
public class SaveDayRecordTask extends AsyncTask<Void, Void, GeneralResponse> {

    private String recordDate;
    private String recordType;
    private String recordValue;
    private Exception exception;
    private Activity activity;
    private SaveDayRecordCallBack callBack;

    public SaveDayRecordTask(Activity activity, String recordDate, String recordType, String recordValue, SaveDayRecordCallBack callBack) {
        this.activity = activity;
        this.recordDate = recordDate;
        this.recordType = recordType;
        this.recordValue = recordValue;
        this.callBack = callBack;
    }

    @Override
    protected GeneralResponse doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try {
            GeneralResponse generalResponse = SpringAndroidService.getInstance(activity.getApplication()).saveDayRecord(recordDate, recordType, recordValue);

            return generalResponse;

        } catch (Exception e) {
            this.exception = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(GeneralResponse generalResponse) {
        if (exception != null) {
            String message;

            if (exception instanceof DuplicateConnectionException) {
                message = "The connection already exists.";
            } else if (exception instanceof ResourceAccessException && exception.getCause() instanceof ConnectTimeoutException) {
                message = "connect time out";
            } else {
                message = "A problem occurred with the network connection. Please try again in a few minutes.";
            }
            callBack.onErrorSaveDayRecord(exception);
        }

        if (generalResponse != null) {
            callBack.onFinishSaveDayRecord(generalResponse.getReturncode());
        }
    }

    public interface SaveDayRecordCallBack {
        public void onFinishSaveDayRecord(int reflag);

        public void onErrorSaveDayRecord(Exception e);
    }
}
