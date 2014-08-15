package com.vee.healthplus.util.user;

import android.app.Activity;
import android.os.AsyncTask;

import com.yunfox.s4aservicetest.response.RegisterResponse;
import com.yunfox.springandroid4healthplus.SpringAndroidService;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

/**
 * Created by wangjiafeng on 13-11-14.
 */
public class RegisterTask extends AsyncTask<Void, Void, Void> {
    private Exception exception;
    private RegisterResponse registerResponse;
    private String username;
    private String password;
    private String verifycode;
    private String nick;
    private Activity activity;
    private RegisterCallBack callBack;
    private SignInTask.SignInCallBack signInCallBack;

    public RegisterTask(Activity activity, String userName, String password, String verifycode, String nick,RegisterCallBack callBack, SignInTask.SignInCallBack signInCallBack) {
        this.activity = activity;
        this.username = userName;
        this.password = password;
        this.verifycode = verifycode;
        this.nick=nick;
        this.callBack = callBack;
        this.signInCallBack = signInCallBack;
    }

    public RegisterTask(Activity activity, String userName, String password, String userNick, RegisterCallBack callBack) {
        this.activity = activity;
        this.username = userName;
        this.password = password;
        this.callBack = callBack;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    @SuppressWarnings("unchecked")
    protected Void doInBackground(Void... params) {
        try {
            registerResponse = SpringAndroidService.getInstance(activity.getApplication()).registerwithverifycodeandnickname(username, verifycode,nick, password);
            System.out.print("registerResponse="+registerResponse.getReturncode());
        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        if (exception != null) {
            String message;
            if (exception instanceof HttpClientErrorException) {
                System.out.println("HttpClientErrorException");
            }
            if (exception instanceof HttpClientErrorException
                    && ((((HttpClientErrorException) exception).getStatusCode() == HttpStatus.BAD_REQUEST)
                    || ((HttpClientErrorException) exception).getStatusCode() == HttpStatus.UNAUTHORIZED)) {
                message = "Your email or password was entered incorrectly.";
            } else if (exception instanceof DuplicateConnectionException) {
                message = "The connection already exists.";
            } else if (exception instanceof ResourceAccessException && exception.getCause() instanceof ConnectTimeoutException) {
                message = "connect time out";
            } else {
                message = "A problem occurred with the network connection. Please try again in a few minutes.";
            }
            registerResponse=null;
            callBack.onErrorRegister(exception);
        }

        if (registerResponse != null) {
            callBack.onFinishRegister(registerResponse.getReturncode());
            if (registerResponse.getReturncode() == 8) {
                HP_User user = new HP_User();
                user.userName = username;
                user.userNick = nick;
                user.userId = Integer.valueOf(String.valueOf(registerResponse.getMemberid()));
                HP_DBModel.getInstance(activity).insertUserInfo(user, true);
                if (signInCallBack != null) {
                    HP_User.setOnLineUserId(activity, Integer.valueOf(String.valueOf(registerResponse.getMemberid())));
                    new SignInTask(activity, username, password, signInCallBack).execute();
                }
            }
        }
    }

    public interface RegisterCallBack {
        public void onFinishRegister(int reflag);

        public void onErrorRegister(Exception e);
    }
}
