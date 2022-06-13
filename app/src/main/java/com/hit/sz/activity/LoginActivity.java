package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hit.sz.R;
import com.hit.sz.webservice.web.WebClientService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnConfirm;
    private Button btnBack;
    private EditText userName;
    private EditText userpwd;
    private String input_name;
    private String input_pwd;

    private WebClientService.WebBinder webBinder;
    private WebClientServConn conn;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_login);
        btnConfirm = findViewById(R.id.confirm_button);
        btnBack = findViewById(R.id.back_button);
        userName = findViewById(R.id.input_name_2);
        userpwd = findViewById(R.id.input_password_2);
        btnConfirm.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        conn = new WebClientServConn();
        Intent intent = new Intent(LoginActivity.this, WebClientService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.i("serv","login try to");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.confirm_button:
                if(login()){
                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    intent = new Intent(LoginActivity.this, UserActivity.class);
                    intent.putExtra("visitor",false);
                    intent.putExtra("userName", input_name);
                    startActivity(intent);
                }
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }


    private boolean login(){
        input_name = userName.getText().toString();
        input_pwd = userpwd.getText().toString();
        if(input_name.equals("") || input_pwd.equals("")){
            Toast.makeText(LoginActivity.this, "请输入有效用户名或密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean isSucc = webBinder.loginVerify(input_name, input_pwd);
        if(!isSucc){
            Toast.makeText(LoginActivity.this, "登录失败：用户名或密码错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
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