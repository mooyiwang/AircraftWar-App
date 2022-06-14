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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hit.sz.R;
import com.hit.sz.webservice.web.WebClientService;

public class WaitingActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isMatched = false;
    private Button btnStartWaiting;
    private TextView txtWating;

    private WebClientService.WebBinder webBinder;
    private WebClientServConn conn;
    private Intent intent;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        btnStartWaiting = findViewById(R.id.start_match_button);
        btnStartWaiting.setOnClickListener(this);
        txtWating = findViewById(R.id.waiting_sign);

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

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.start_match_button){
            webBinder.playerMatch(handler);
            txtWating.setText("匹配中，请稍后..");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return true;
    }
}