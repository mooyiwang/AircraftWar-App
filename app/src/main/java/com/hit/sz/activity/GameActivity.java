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
import android.os.Looper;
import android.os.Message;
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
    public static GameActivity instance;
    private AbstractGameView game;


    static {
        isBattle = false;
    }
    public WebClientService.WebBinder webBinder;
    private WebClientServConn conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        instance = this;

        Intent intent2 = getIntent();
        Bundle extras = intent2.getExtras();
        isBattle = extras.getBoolean("isBattle");

        conn = new WebClientServConn();
        Intent intent = new Intent(GameActivity.this, WebClientService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);

        if(isBattle){
            game = new MediumGameView(this);
            setContentView(game);

            new Thread(()->{
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                webBinder.playerCommu(game);
            }).start();

        }
        else{
            switch (LevelSoundActivity.GAME_LEVEL){
                case 0:
                    game = new EasyGameView(this);
                    setContentView(game);
                    break;
                case 1:
                    game = new MediumGameView(this);
                    setContentView(game);
                    break;
                case 2:
                    game = new HardGameView(this);
                    setContentView(game);
                    break;
            }
        }


        new Thread(()->{
            while(true){
                if(isBattle==false){
                    if(game.gameOverFlag==true){
                        Looper.prepare();
                        Toast.makeText(GameActivity.this, "游戏结束", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;
                    }
                }
                else{
                    if(game.gameOverFlag==true && game.isBattleFinish==false){
                        Looper.prepare();
                        Toast.makeText(GameActivity.this, "对战失败！游戏结束", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;

                    }
                    if(game.gameOverFlag==false && game.isBattleFinish==true){
                        Looper.prepare();
                        Toast.makeText(GameActivity.this, "恭喜！对战成功！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;
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