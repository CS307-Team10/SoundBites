package com.soundbytes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity implements View.OnClickListener{
    Button bRegister;
    EditText etName, etAge, etUsername, etPassword;
    private enum Field {Name, UserName, Age, Password};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = (EditText)findViewById(R.id.etName);
        etAge = (EditText)findViewById(R.id.etAge);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        bRegister = (Button)findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
    }

    private void compulsoryField(Field field){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Ooops!");
        alertDialog.setMessage(getResources().getString(R.string.compulsory, field));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bRegister:
                String name = etName.getText().toString().trim();
                if(name.equals("")) {
                    compulsoryField(Field.Name);
                    break;
                }
                String username = etUsername.getText().toString().trim();
                if(username.equals("")) {
                    compulsoryField(Field.UserName);
                    break;
                }
                String password = etPassword.getText().toString().trim();
                if(password.equals("")) {
                    compulsoryField(Field.Password);
                    break;
                }
                String ageString = etAge.getText().toString().trim();
                if(ageString.equals("")) {
                    compulsoryField(Field.Age);
                    break;
                }
                int age = Integer.parseInt(ageString);

                User user = new User(name, age, username, password);
                registerUser(user);
                break;
        }
    }

    private void registerUser(User user) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                Toast.makeText(Register.this, "Successfully registered", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
