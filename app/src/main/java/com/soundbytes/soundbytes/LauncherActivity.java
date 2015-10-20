package com.soundbytes.soundbytes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener{

    Button bMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        bMainActivity = (Button)findViewById(R.id.bMainActivity);
        bMainActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bMainActivity:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(this, Login.class));
    }
}
