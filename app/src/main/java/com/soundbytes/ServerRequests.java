package com.soundbytes;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import java.util.ArrayList;

public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://naveenganessin.com/";
    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("please wait");
    }
    public void storeUserDataInBackground(User user, GetUserCallBack userCallBack){
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallBack).execute();
    }
    public void fetchUserDataInBackground(User user, GetUserCallBack callBack){
        progressDialog.show();
        new fetchUserDataAsyncTask(user, callBack).execute();
    }

    public void addFriendDataInBackground(User user, GetUserCallBack callBack1){
        progressDialog.show();
        new addFriendDataAsyncTask(user, callBack1).execute();
    }

    public void storeUserKeyInBackground(User user, String key, GetUserCallBack callBack){
        new StoreUserGCMKey(user, key, callBack).execute();
    }
    public void fetchAudioFileInBackground(User user, String audioID, OnAudioDownloadCallback callBack){
        new FetchAudioFile(user, audioID, callBack).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void>{
        User user;
        GetUserCallBack userCallBack;
        public StoreUserDataAsyncTask(User user, GetUserCallBack userCallBack){
            this.user = user;
            this.userCallBack = userCallBack;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> datatoSend = new ArrayList<>();
            datatoSend.add(new BasicNameValuePair("name", user.name));
            datatoSend.add(new BasicNameValuePair("age", user.age + ""));
            datatoSend.add(new BasicNameValuePair("username", user.username));
            datatoSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+ "Register.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(datatoSend));
                client.execute(post);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallBack.done(null);
            super.onPostExecute(aVoid);
        }
    }
    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallBack;

        public fetchUserDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> datatoSend = new ArrayList<>();

            datatoSend.add(new BasicNameValuePair("username", user.username));
            datatoSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+ "FetchUserData.php");

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(datatoSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);
                if(jObject.length() == 0){
                    returnedUser = null;
                } else{
                    String name = jObject.getString("name");
                    int age = jObject.getInt("age");
                    returnedUser = new User(name,age,user.username, user.password);

                }
            }catch(Exception e){
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }

    public class StoreUserGCMKey extends AsyncTask<Void, Void, Void>{
        User user;
        String key;
        GetUserCallBack userCallBack;

        public StoreUserGCMKey(User user, String key, GetUserCallBack userCallBack){
            this.user = user;
            this.key = key;
            this.userCallBack = userCallBack;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> datatoSend = new ArrayList<>();
            datatoSend.add(new BasicNameValuePair("username", user.username));
            datatoSend.add(new BasicNameValuePair("key", key));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+ "SaveKey.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(datatoSend));
                client.execute(post);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            userCallBack.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class FetchAudioFile extends AsyncTask<Void, Void, Void> {
        OnAudioDownloadCallback audioCallBack;
        User user;
        String audio_ID;
        String b64;

        public FetchAudioFile(User user, String audio_ID, OnAudioDownloadCallback audioCallBack) {
            this.user = user;
            this.audio_ID = audio_ID;
            this.audioCallBack = audioCallBack;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> datatoSend = new ArrayList<>();
            datatoSend.add(new BasicNameValuePair("username", user.username));
            datatoSend.add(new BasicNameValuePair("audioID", audio_ID));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "GetAudio.php");
            try {
                post.setEntity(new UrlEncodedFormEntity(datatoSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);
                if (jObject.length() == 0) {
                    b64 = null;
                } else {
                    b64 = jObject.getString("b64");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            audioCallBack.onAudioDownloaded(b64);
            super.onPostExecute(aVoid);
        }
    }

    public class addFriendDataAsyncTask extends AsyncTask<Void, Void, User> {
        User user;
        GetUserCallBack userCallBack;

        public addFriendDataAsyncTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> datatoSend = new ArrayList<>();

            datatoSend.add(new BasicNameValuePair("username", user.username));
            datatoSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+ "addFriend.php");

            User returnedUser = null;

            try {
                post.setEntity(new UrlEncodedFormEntity(datatoSend));
                HttpResponse httpResponse = client.execute(post);
                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jObject = new JSONObject(result);
                if(jObject.length() == 0){
                    returnedUser = null;
                } else{
                    String name = jObject.getString("name");
                    int age = jObject.getInt("age");
                    returnedUser = new User(name,age,user.username, user.password);
                }
                System.out.println("result: " + result);
            }catch(Exception e){
                e.printStackTrace();
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallBack.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}
