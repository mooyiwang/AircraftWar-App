package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.widget.Toast;

import com.hit.sz.R;
import com.hit.sz.view.AbstractGameView;
import com.hit.sz.view.EasyGameView;
import com.hit.sz.view.HardGameView;
import com.hit.sz.view.MediumGameView;
import com.hit.sz.webservice.web.WebClientService;

public class GameActivity extends AppCompatActivity {

    public static boolean isBattle;


    static {
        isBattle = false;
    }
    public WebClientService.WebBinder webBinder;
    private WebClientServConn conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent2 = getIntent();
        Bundle extras = intent2.getExtras();
        isBattle = extras.getBoolean("isBattle");

        conn = new WebClientServConn();
        Intent intent = new Intent(GameActivity.this, WebClientService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);

        if(isBattle){
            AbstractGameView newGame = new MediumGameView(this);
            setContentView(newGame);

            new Thread(()->{
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                webBinder.playerCommu(newGame);
            }).start();

        }
        else{
            switch (LevelSoundActivity.GAME_LEVEL){
                case 0:
                    setContentView(new EasyGameView(this));
                    break;
                case 1:
                    setContentView(new MediumGameView(this));
                    break;
                case 2:
                    setContentView(new HardGameView(this));
                    break;
            }
        }

        new Thread(()->{
            while(true){
                if(isBattle==false){
                    if(AbstractGameView.gameOverFlag==true){
                        Toast.makeText(GameActivity.this, "游戏结束", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    if(AbstractGameView.gameOverFlag==true && AbstractGameView.isBattleFinish==false){
                        Toast.makeText(GameActivity.this, "对战失败！游戏结束", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if(AbstractGameView.gameOverFlag==false && AbstractGameView.isBattleFinish==true){
                        Toast.makeText(GameActivity.this, "恭喜！对战成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }).start();

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return true;
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