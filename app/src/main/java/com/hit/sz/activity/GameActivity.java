package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;

import com.hit.sz.R;
import com.hit.sz.application.ImageManager;
import com.hit.sz.application.MainActivity;
import com.hit.sz.view.EasyGameView;
import com.hit.sz.view.HardGameView;
import com.hit.sz.view.MediumGameView;

public class GameActivity extends AppCompatActivity {
    /*
    ** 音乐相关
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        loading_img();
        switch (MainActivity.GAME_LEVEL){
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



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return true;
    }



    public void loading_img(){
        ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_enemy);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.mob);
        ImageManager.ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.elite);
        ImageManager.BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.boss);
        ImageManager.BLOOD_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_blood);
        ImageManager.BOMB_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bomb);
        ImageManager.FIRE_PROP_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bullet);

    }
}