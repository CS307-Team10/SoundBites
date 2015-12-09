package com.soundbytes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.soundbytes.db.FeedDatabaseHandler;

import java.io.IOException;

/**
 * Created by Olumide on 11/2/2015.
 */
public class GCMRegistration implements GCMKeyCallback {
    private Context context = null;
    private ProgressDialog progressDialog = null;
    private OnKeyStoredCallback callback;
    private User user;
    private SharedPreferences prefs;

    public static boolean isGCMRegistered(Context context){
        return context.getSharedPreferences(SoundByteConstants.PACKAGE_NAME, Context.MODE_PRIVATE)
                .getBoolean(SoundByteConstants.IS_GCM_KEY_STORED, false);
    }

    public GCMRegistration(Context context, User user, OnKeyStoredCallback callback){
        this.context = context;
        this.callback = callback;
        this.user = user;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("One moment");
        progressDialog.setMessage("Please wait");
        this.prefs = context.getSharedPreferences(SoundByteConstants.PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public void register(){
        progressDialog.show();
        new GetGCMKeyAsyncTask(this, context).execute();
    }

    public void logout(){
        progressDialog.show();
        FeedDatabaseHandler.logout(context);
        new DeleteGCMKeyAsyncTask(this, context).execute();
    }

    public void onKeyDeleted(){
        prefs.edit().putBoolean(SoundByteConstants.IS_GCM_KEY_STORED, false).apply();
        ServerRequests serverRequests = new ServerRequests(context);
        serverRequests.storeUserKeyInBackground(user, "", new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                callback.onKeyStored(true);
                progressDialog.dismiss();
            }
        });
    }

    public void onKeyReturned(String key){
        if (key == null) {
            prefs.edit().putBoolean(SoundByteConstants.IS_GCM_KEY_STORED, false).apply();
            progressDialog.dismiss();
            callback.onKeyStored(false);
        }else {
            prefs.edit().putBoolean(SoundByteConstants.IS_GCM_KEY_STORED, true).apply();
            ServerRequests serverRequests = new ServerRequests(context);
            serverRequests.storeUserKeyInBackground(user, key, new GetUserCallBack() {
                @Override
                public void done(User returnedUser) {
                    callback.onKeyStored(true);
                    progressDialog.dismiss();
                }
            });
        }
    }

    public interface OnKeyStoredCallback {
        void onKeyStored(boolean stored);
    }

    public class GetGCMKeyAsyncTask extends AsyncTask<Void, Void, Void> {
        private GCMKeyCallback callback;
        private Context context;
        private String token = null;

        public GetGCMKeyAsyncTask(GCMKeyCallback callback, Context context){
            this.callback = callback;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                token = InstanceID.getInstance(context).getToken(context.getString(R.string.gcm_sender_id),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            }catch (IOException ex){
                Log.e("GCM", "Oops some error occurred");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            callback.onKeyReturned(token);
        }
    }

    public class DeleteGCMKeyAsyncTask extends AsyncTask<Void, Void, Void> {
        private GCMKeyCallback callback;
        private Context context;

        public DeleteGCMKeyAsyncTask(GCMKeyCallback callback, Context context){
            this.callback = callback;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                InstanceID.getInstance(context).deleteToken(context.getString(R.string.gcm_sender_id),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            }catch (IOException ex){
                Log.e("GCM", "Oops some error occurred");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            callback.onKeyDeleted();
        }
    }
}
