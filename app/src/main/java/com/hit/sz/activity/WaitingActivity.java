package com.hit.sz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.hit.sz.R;
import com.hit.sz.webservice.web.WebClientService;

public class WaitingActivity extends AppCompatActivity {

    private boolean isMatched = false;

    private WebClientService.WebBinder webBinder;
    private WebClientServConn conn;
    private Intent intent;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        isMatched = false;

        conn = new WebClientServConn();
        Intent intent = new Intent(WaitingActivity.this, WebClientService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);

        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==1){
                    isMatched = (boolean) msg.obj;
                }
            }
        };

        webBinder.playerMatch(handler);

        new Thread(()->{
            while(true){
                if(isMatched){
                    Intent intent1 = new Intent(WaitingActivity.this, GameActivity.class);
                    intent1.putExtra("isBattle", true);
                    startActivity(intent1);
                    finish();
                }
            }
        }).start();
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