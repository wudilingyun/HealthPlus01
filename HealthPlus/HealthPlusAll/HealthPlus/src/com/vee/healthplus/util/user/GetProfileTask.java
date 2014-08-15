package com.vee.healthplus.util.user;

import android.app.Activity;
import android.os.AsyncTask;

import com.yunfox.springandroid4healthplus.SpringAndroidService;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.greenhouse.api.Profile;
import org.springframework.web.client.ResourceAccessException;

/**
 * Created by wangjiafeng on 13-11-14.
 */
public class GetProfileTask extends AsyncTask<Void, Void, Profile> {

    private Exception exception;
    private Activity activity;
    private GetProfileCallBack callBack;

    public GetProfileTask(Activity activity, GetProfileCallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        this.exception = null;
    }

    @Override
    protected Profile doInBackground(Void... arg0) {
        // TODO Auto-generated method stub
        try {
            Profile profile = SpringAndroidService.getInstance(
                    activity.getApplication()).getProfileDetail();

            return profile;

        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Profile profile) {
        if (exception != null) {
            String message;

            if (exception instanceof DuplicateConnectionException) {
                message = "The connection already exists.";
            } else if (exception instanceof ResourceAccessException && exception.getCause() instanceof ConnectTimeoutException) {
                message = "connect time out";
            } else {
                message = "A problem occurred with the network connection. Please try again in a few minutes.";
            }
            callBack.onErrorGetProfile(exception);
        }

        if (profile != null) {
            callBack.onFinishGetProfile(profile);
        }
    }

    public interface GetProfileCallBack {
        public void onFinishGetProfile(Profile profile);

        public void onErrorGetProfile(Exception e);
    }
}
