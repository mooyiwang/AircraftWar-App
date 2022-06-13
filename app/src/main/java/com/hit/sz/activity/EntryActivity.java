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

import com.hit.sz.R;
import com.hit.sz.webservice.data.SignupData;
import com.hit.sz.webservice.web.WebClientService;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login; //登录
    private Button signup; //注册
    private Button visitor;
    private Intent intent;

    private WebClientService.WebBinder webBinder;
    private WebClientServConn conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        visitor = findViewById(R.id.visitor);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        visitor.setOnClickListener(this);

        conn = new WebClientServConn();
        Intent intent = new Intent(EntryActivity.this, WebClientService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.i("serv","try to");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                intent = new Intent(EntryActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.signup:
                intent = new Intent(EntryActivity.this, SignupActivity.class);
                startActivity(intent);
                break;
            case R.id.visitor:
                intent = new Intent(EntryActivity.this, UserActivity.class);
                intent.putExtra("isVisitor", true);
                startActivity(intent);
                break;
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