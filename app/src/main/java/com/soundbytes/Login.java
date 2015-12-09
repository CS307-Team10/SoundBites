package com.soundbytes;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.ActionBar;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegisterLink;
    UserLocalStore userLocalStore;
    private enum Field{UserName, Password};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        bLogin = (Button)findViewById(R.id.bLogin);
        tvRegisterLink = (TextView)findViewById(R.id.tvRegisterLink);
        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(username.trim().equals("")){
                    compulsoryField(Field.UserName);
                    break;
                }
                if(password.trim().equals("")){
                    compulsoryField(Field.Password);
                    break;
                }
                User user = new User(username, password);
                authenticate(user);
                //userLocalStore.storeUserData(user);
                //userLocalStore.setUserLoggedin(true);



                break;
            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void compulsoryField(Field field){

    }

    private void validateFields(){

    }

    private void authenticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(final User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    new GCMRegistration(Login.this, returnedUser, new GCMRegistration.OnKeyStoredCallback(){
                        @Override
                        public void onKeyStored(boolean stored){
                            logUserIn(returnedUser);
                        }
                    }).register();                }
            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("incorrect user details");
        dialogBuilder.setPositiveButton("ok", null);
        dialogBuilder.show();
    }
    private void logUserIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedin(true);

        startActivity(new Intent(this, MainActivity.class));
        finish();
        System.out.println("changing to mainActivity");
        System.out.println("name: " + returnedUser.name);
    }
}
