package com.soundbytes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class SendActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    private static final String SERVER_ADDRESS = "http://naveenganessin.com/";
    Button bUploadImage, bMainBack, bSendFriend;
    EditText uploadImageName, FriendNameSend;
    File file = null;
    UserLocalStore userLocalStore;
    String selectedPath = Environment.getExternalStorageDirectory() + "/audiorecordtest.3gp";
    User cUser;
    String uName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        bUploadImage = (Button) findViewById(R.id.bUploadImage);
        bMainBack = (Button) findViewById(R.id.bMainBack);
        bSendFriend = (Button)findViewById(R.id.bSendFriend);
        uploadImageName = (EditText) findViewById(R.id.etUploadName);
        FriendNameSend = (EditText) findViewById(R.id.etFriendNameSend);
        bUploadImage.setOnClickListener(this);
        bMainBack.setOnClickListener(this);
        bSendFriend.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        uName = bundle.getString("uName");
        System.out.println("name: " + uName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bUploadImage:
                file = new File(Environment.getExternalStorageDirectory() + "/audiorecordtest.3gp");
                System.out.println(Environment.getExternalStorageDirectory() + "/audiorecordtest.3gp");
                if (file == null) {
                    System.out.println("no file made");
                } else {
                    System.out.println("file made");
                }
                new UploadImage(file, uploadImageName.getText().toString()).execute();
                break;
            case R.id.bMainBack:
                startActivity(new Intent(SendActivity.this, MainActivity.class));
                break;
            case R.id.bSendFriend:
                //System.out.println(userLocalStore.getLoggedInUser().name);
                System.out.println(FriendNameSend.getText().toString());
                System.out.println("inside bPress: " + uName);
                new uploadFriendDetails(uName , FriendNameSend.getText().toString(), 0, 0, 0, 0).execute();
                break;
        }
    }

    private class uploadFriendDetails extends AsyncTask<Void, Void, Void> {
        String userName;
        String friendName;
        int highPitch;
        int lowPitch;
        int speedUp;
        int slowDown;

        public uploadFriendDetails(String userName, String friendName, int highPitch, int lowPitch, int speedUp, int slowDown) {
            this.userName = userName;
            this.friendName = friendName;
            this.highPitch = highPitch;
            this.lowPitch = lowPitch;
            this.speedUp = speedUp;
            this.slowDown = slowDown;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> datatoSend = new ArrayList<>();
            datatoSend.add(new BasicNameValuePair("userName", userName));
            datatoSend.add(new BasicNameValuePair("friendName", friendName));
            datatoSend.add(new BasicNameValuePair("highPitch", highPitch + ""));
            datatoSend.add(new BasicNameValuePair("lowPitch", lowPitch+""));
            datatoSend.add(new BasicNameValuePair("speedUp", speedUp+""));
            datatoSend.add(new BasicNameValuePair("slowDown", slowDown+""));


            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+ "updateUserDetails.php");
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
            super.onPostExecute(aVoid);
            System.out.println("Friend Details Uploaded");
            Toast.makeText(getApplicationContext(), "Friend Details Uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    private class UploadImage extends AsyncTask<Void, Void, Void> {
        File image;
        String name;

        public UploadImage(File image, String name) {
            this.image = image;
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String encodedImage = "";
            FileInputStream fis;
            try {
                System.out.println("video array");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                System.out.println("video array");
                System.out.println(selectedPath);
                fis = new FileInputStream(file);
                System.out.println("video array");
                byte[] buf = new byte[1024];
                int n;
                while (-1 != (n = fis.read(buf)))
                    baos.write(buf, 0, n);
                System.out.println("video array");
                encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                //System.out.println("video array"+encodedImage);

            } catch (Exception e) {
                System.out.println("byte array exception triggered");
                e.printStackTrace();
            }
            System.out.println("test1");
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", name));
            System.out.println("test2");
            HttpParams httpRequestParams = getHttpRequestParams();
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "SavePicture.php");
            System.out.println("test3");
            try {
                System.out.println("test6");
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                System.out.println("test4");
                client.execute(post);
                System.out.println("test5");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println("Audio Uploaded");
            Toast.makeText(getApplicationContext(), "Audio Uploaded", Toast.LENGTH_SHORT).show();
        }
    }
    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 60);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 60);
        return httpRequestParams;
    }
}