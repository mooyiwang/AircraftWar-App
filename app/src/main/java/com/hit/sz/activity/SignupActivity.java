package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hit.sz.R;
import com.hit.sz.webservice.web.WebClientService;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnConfirm;
    private Button btnBack;
    private EditText userName;
    private EditText userpwd;
    private EditText userpwd_2;
    private String input_name;
    private String input_pwd;
    private String input_pwd_2;

    private WebClientService.WebBinder webBinder;
    private WebClientServConn conn;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnConfirm = findViewById(R.id.confirm_button_2);
        btnBack = findViewById(R.id.back_button_2);
        userName = findViewById(R.id.input_name_2);
        userpwd = findViewById(R.id.input_password_2);
        userpwd_2 = findViewById(R.id.input_pwd_again);
        btnBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        conn = new WebClientServConn();
        Intent intent = new Intent(SignupActivity.this, WebClientService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_button_2:
                intent = new Intent(SignupActivity.this, EntryActivity.class);
                startActivity(intent);
                break;
            case R.id.confirm_button_2:
                if(signup()){
                    Toast.makeText(this, "注册成功！请登录。", Toast.LENGTH_SHORT).show();
                    intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private boolean signup(){
        input_name = userName.getText().toString();
        input_pwd = userpwd.getText().toString();
        input_pwd_2 = userpwd_2.getText().toString();
        if(!input_pwd_2.equals(input_pwd)){
            Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            if(webBinder.signupNameCheck(input_name)){
                return webBinder.signup(input_name, input_pwd);
            }
            else{
                Toast.makeText(this, "用户名已重复", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
    class WebClientServConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            webBinder = (WebClientService.WebBinder)iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }
}