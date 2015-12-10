package com.soundbytes;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class LauncherActivity extends AppCompatActivity {
    protected FilterManager.MyRunnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        runnable = new FilterManager.MyRunnable() {
            boolean stopped = false;
            @Override
            public void stop() {
                stopped = true;
            }

            @Override
            public void run() {
                if (!stopped)
                    launchMainActivity();
            }
        };
        new Handler().postDelayed(runnable, 3000);
        findViewById(R.id.splash_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.stop();
                launchMainActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return false;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * This is what launches the mainActivity, this methid is attached to the button in the xml file
     * @param v the view that initiated this method
     */
    public void launchMainActivity(View v){
        launchMainActivity();
    }
}
