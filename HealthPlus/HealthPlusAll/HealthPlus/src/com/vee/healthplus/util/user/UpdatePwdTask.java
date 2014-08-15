package com.vee.healthplus.util.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.yunfox.s4aservicetest.response.GeneralResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.ResourceAccessException;

/**
 * Created by wangjiafeng on 13-11-18.
 */
public class UpdatePwdTask extends AsyncTask<Void, Void, GeneralResponse> {
    private Exception exception;
    private String oldpassword;
    private String newpassword;
    private Activity activity;
    private UpdatePwdCallBack callBack;

    public UpdatePwdTask(Activity activity, String oldpassword, String newpassword, UpdatePwdCallBack callBack) {
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected GeneralResponse doInBackground(Void... params) {
        // TODO Auto-generated method stub
        try {
            Log.i("soda_test",oldpassword+"***"+newpassword);
            GeneralResponse generalResponse = SpringAndroidService.getInstance(activity.getApplication()).updatePassword(oldpassword, newpassword);
            Log.e("xuxuxu","generalResponse="+generalResponse+"");
            return generalResponse;
        } catch (Exception e) {
            Log.e("xuxuxu", e.getMessage());
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
            callBack.onErrorUpdatePwd(exception);
        }

        if (generalResponse != null) {
            callBack.onFinishUpdatePwd(generalResponse.getReturncode());
        }
    }

    public interface UpdatePwdCallBack {
        public void onFinishUpdatePwd(int reflag);

        public void onErrorUpdatePwd(Exception e);
    }
}
