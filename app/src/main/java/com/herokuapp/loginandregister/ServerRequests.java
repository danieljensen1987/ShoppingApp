package com.herokuapp.loginandregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ServerRequests {
    ProgressDialog dialog;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    //public static final String SERVER_ADDRESS = "https://shoppingapi.herokuapp.com";
    public static final String SERVER_ADDRESS = "http://192.168.1.143:3000";

    public ServerRequests(Context context){
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setTitle("Processing");
        dialog.setMessage("Please wait...");
    }

    public void createUser(Credentials user, GetUserCallback callback){
        dialog.show();
        new CreateUserAsync(user, callback ).execute();
    }

    public void login(Credentials user, GetUserCallback callback){
        dialog.show();
        new Login(user, callback ).execute();
    }

    public class CreateUserAsync extends AsyncTask<String, Void, String>{
        Credentials user;
        GetUserCallback userCallback;

        public CreateUserAsync(Credentials user, GetUserCallback callback){
            this.user = user;
            this.userCallback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String uid = "";
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/newuser");

            JSONObject obj = new JSONObject();
            try {
                obj.put("email", user.email);
                obj.put("password", user.password);

                StringEntity se = new StringEntity(obj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                HttpResponse response = client.execute(post);

                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() != 0){
                    uid = jsonObject.getString(("_id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return uid;
        }

        @Override
        protected void onPostExecute(String uid) {
            dialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(uid);
        }
    }

    public class Login extends AsyncTask<String, Void, String> {
        Credentials creds;
        GetUserCallback userCallback;

        public Login(Credentials creds, GetUserCallback callback) {
            this.creds = creds;
            this.userCallback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String uid = null;
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "/login");

            JSONObject obj = new JSONObject();
            try {
                obj.put("email", creds.email);
                obj.put("password", creds.password);

                StringEntity se = new StringEntity(obj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                HttpResponse response = client.execute(post);

                if(response.getStatusLine().getStatusCode() == 200){
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    JSONObject jsonObject = new JSONObject(result);
                    uid = jsonObject.getString(("_id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uid;
        }

        @Override
        protected void onPostExecute(String uid) {
            dialog.dismiss();
            userCallback.done(uid);
            super.onPostExecute(uid);
        }
    }
}