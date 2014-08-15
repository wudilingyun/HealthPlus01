package com.vee.healthplus.ui.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.Toast;

import org.springframework.http.HttpStatus;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

/**
 * Created by wangjiafeng on 13-11-11.
 */
public class UserBaseFragment extends Fragment {

    private ProgressDialog progressDialog;
    private Context mContext;


    //***************************************
    // GreenhouseActivity methods
    //***************************************

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = getActivity();
    }

    public void showProgressDialog() {
        showProgressDialog("Loading. Please wait...");
    }

    public void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    //***************************************
    // Protected methods
    //***************************************
    protected void processException(Exception e) {
        if (e != null) {
            if (e instanceof ResourceAccessException) {
                displayNetworkError();
            } else if (e instanceof HttpClientErrorException) {
                HttpClientErrorException httpError = (HttpClientErrorException) e;
                if (httpError.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    displayAuthorizationError();
                }
            } else if (e instanceof MissingAuthorizationException) {
                displayAuthorizationError();
            }
        }
    }

    protected void displayNetworkError() {
        Toast toast = Toast.makeText(mContext, "A problem occurred with the network connection while attempting to communicate with Greenhouse.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    protected void displayAuthorizationError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("You are not authorized to connect to Greenhouse. Please reauthorize the app.");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                signOut();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void signOut() {
    }
}
