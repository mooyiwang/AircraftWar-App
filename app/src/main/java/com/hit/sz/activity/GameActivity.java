package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.hit.sz.R;
import com.hit.sz.view.AbstractGameView;
import com.hit.sz.view.EasyGameView;
import com.hit.sz.view.HardGameView;
import com.hit.sz.view.MediumGameView;

public class GameActivity extends AppCompatActivity {

    public static boolean isBattle;


    static {
        isBattle = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent2 = getIntent();
        Bundle extras = intent2.getExtras();
        isBattle = extras.getBoolean("isBattle");

        if(isBattle){
            setContentView(new MediumGameView(this));
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
                if(AbstractGameView.gameOverFlag==true){
                    finish();
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

}