package com.vee.healthplus.util.user;

import android.app.Activity;
import android.os.AsyncTask;

import com.yunfox.s4aservicetest.response.DayRecord;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

/**
 * Created by wangjiafeng on 13-11-20.
 */
public class QueryAllDayRecordByType extends AsyncTask<Void, Void, List<DayRecord>> {
    private Exception exception;
    private String recordType;
    private Activity activity;
    private QueryAllDayRecordByTypCallBack callback;

    public QueryAllDayRecordByType(Activity activity, String recordType, QueryAllDayRecordByTypCallBack callback) {
        this.activity = activity;
        this.recordType = recordType;
        this.callback = callback;
    }

    @Override
    protected List<DayRecord> doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try {
            List<DayRecord> dayrecordlist = SpringAndroidService.getInstance(activity.getApplication()).getAllDayRecordListByType(recordType);

            return dayrecordlist;

        } catch (Exception e) {
            this.exception = e;
        }

        return null;
    }


    @Override
    protected void onPostExecute(List<DayRecord> dayrecordlist) {
        if (exception != null) {
            String message;

            if (exception instanceof DuplicateConnectionException) {
                message = "The connection already exists.";
            } else if (exception instanceof ResourceAccessException && exception.getCause() instanceof ConnectTimeoutException) {
                message = "connect time out";
            } else {
                message = "A problem occurred with the network connection. Please try again in a few minutes.";
            }
            callback.onErrorQueryAllDayRecordByTyp(exception);
        }

        if (dayrecordlist != null) {
            callback.onFinishQueryAllDayRecordByTyp(dayrecordlist);
        }
    }

    public interface QueryAllDayRecordByTypCallBack {
        public void onFinishQueryAllDayRecordByTyp(List<DayRecord> dayrecordlist);

        public void onErrorQueryAllDayRecordByTyp(Exception e);
    }
}
