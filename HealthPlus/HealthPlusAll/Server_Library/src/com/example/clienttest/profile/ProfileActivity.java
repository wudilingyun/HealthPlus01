package com.example.clienttest.profile;

import java.util.Map;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.greenhouse.api.Greenhouse;
import org.springframework.social.greenhouse.api.Detail;
import org.springframework.social.greenhouse.api.DetailResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.example.clienttest.AbstractGreenhouseActivity;
import com.example.clienttest.R;
import com.example.clienttest.R.layout;
import com.example.clienttest.R.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ProfileActivity extends AbstractGreenhouseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViewById(R.id.buttonsignout).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        signOut();
                    }
                });

        findViewById(R.id.buttonprofilesave).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Auto-generated method stub
                        new SaveProfileTask().execute();
                    }
                });

        new GetProfileTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    private void displayProfileMessage(String message) {
        new AlertDialog.Builder(this).setMessage(message).setCancelable(false)
                .setPositiveButton("OK", null).create().show();
    }

    // ***************************************
    // Private classes
    // ***************************************
    private class SaveProfileTask extends AsyncTask<Void,Void, DetailResponse>{
        private MultiValueMap<String,String> formData;
        private Exception exception;

        @Override
        protected void onPreExecute(){
            formData = new LinkedMultiValueMap<String, String>();

            EditText editTextUsername = (EditText) findViewById(R.id.editTextUsername);
            EditText editTextNickname = (EditText)findViewById(R.id.editTextNickname);
            EditText editTextEmail = (EditText)findViewById(R.id.editTextEmail);
            EditText editTextPhone = (EditText)findViewById(R.id.editTextPhone);
            //gender later
            EditText editTextWeight = (EditText)findViewById(R.id.editTextWeight);
            EditText editTextHeight = (EditText)findViewById(R.id.editTextHeight);

            String username = editTextUsername.getText().toString().trim();
            formData.add("username", username);

            String nickname = editTextNickname.getText().toString().trim();
            formData.add("nickname", nickname);

            String email = editTextEmail.getText().toString().trim();
            formData.add("email", email);

            String phone = editTextPhone.getText().toString().trim();
            formData.add("phone", phone);

            String weight = editTextWeight.getText().toString().trim();
            formData.add("weight", weight);

            String height = editTextHeight.getText().toString().trim();
            formData.add("height", height);
        }

        @Override
        protected DetailResponse doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try{
                checkConnection();

                DetailResponse detailresponse = getApplicationContext().getPrimaryConnection().getApi().userOperations().saveDetail(formData);
                Log.d(TAG,String.valueOf(detailresponse.getReturncode()));

                return detailresponse;

            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                this.exception = e;
            }

            return null;
        }

        protected void checkConnection()
        {
            if(getApplicationContext().getConnectionRepository().findPrimaryConnection(Greenhouse.class) == null)
            {
                System.out.println("no primary connection ............");
                throw new MissingAuthorizationException();
            }
        }

        @Override
        protected void onPostExecute(DetailResponse profileresponse)
        {
            if(profileresponse != null)
            {
                switch(profileresponse.getReturncode())
                {
                    case 200:
                        displayProfileMessage("保存成功");
                }
            }
        }

    }

    private class GetProfileTask extends AsyncTask<Void,Void,Detail>{
        private Exception exception;

        @Override
        protected void onPreExecute(){
            this.exception = null;
        }

        @Override
        protected Detail doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            try{
                checkConnection();

                Detail detail = getApplicationContext().getPrimaryConnection().getApi().userOperations().getDetail();
                Log.d(TAG,detail.getUsername());

                return detail;

            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                this.exception = e;
            }
            return null;
        }

        protected void checkConnection()
        {
            if(getApplicationContext().getConnectionRepository().findPrimaryConnection(Greenhouse.class) == null)
            {
                System.out.println("no primary connection ............");
                throw new MissingAuthorizationException();
            }
        }

        @Override
        protected void onPostExecute(Detail profile)
        {
            if(profile != null)
            {
                EditText editTextUsername = (EditText) findViewById(R.id.editTextUsername);
                EditText editTextNickname = (EditText)findViewById(R.id.editTextNickname);
                EditText editTextEmail = (EditText)findViewById(R.id.editTextEmail);
                EditText editTextPhone = (EditText)findViewById(R.id.editTextPhone);
                //gender later
                EditText editTextWeight = (EditText)findViewById(R.id.editTextWeight);
                EditText editTextHeight = (EditText)findViewById(R.id.editTextHeight);

                if(profile.getUsername() != null && profile.getUsername().length() > 0)
                {
                    editTextUsername.setText(profile.getUsername());
                }
                if(profile.getNickname() != null && profile.getNickname().length() > 0)
                {
                    editTextNickname.setText(profile.getNickname());
                }
                if(profile.getEmail() != null && profile.getEmail().length() > 0)
                {
                    editTextEmail.setText(profile.getEmail());
                }
                if(profile.getPhone() != null && profile.getPhone().length() > 0)
                {
                    editTextPhone.setText(profile.getPhone());
                }
                if(profile.getWeight() != null && profile.getWeight() > 0.1 )
                {
                    editTextWeight.setText(String.valueOf(profile.getWeight()));
                }
                if( profile.getHeight() > 0)
                {
                    editTextHeight.setText(String.valueOf(profile.getHeight()));
                }
            }
        }


    }
}
