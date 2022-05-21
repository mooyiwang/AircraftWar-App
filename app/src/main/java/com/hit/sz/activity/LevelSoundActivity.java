package com.hit.sz.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.hit.sz.R;

public class LevelSoundActivity extends AppCompatActivity {


    private Button easyButton;
    private Button mediumButton;
    private Button hardButton;
    private Switch musicSwitch;


    //屏幕宽高
    public static int screenWidth;
    public static int screenHeight;

    //音效,难度,得分
    public static boolean GAME_SOUND = false;
    public static int GAME_LEVEL = 0;
    public static int SCORE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenHW();
        setContentView(R.layout.levelsound_main);
        easyButton = findViewById(R.id.easy);
        mediumButton = findViewById(R.id.medium);
        hardButton = findViewById(R.id.hard);
        musicSwitch = findViewById(R.id.musicSwitch);
        easyButton.setOnClickListener(new ButtonListener());
        mediumButton.setOnClickListener(new ButtonListener());
        hardButton.setOnClickListener(new ButtonListener());

        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                LevelSoundActivity.GAME_SOUND = isChecked;
            }
        });

    }

    /**
     * 利用DisplayMetrics 获取屏幕宽高
     */
    public void getScreenHW(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }


    private class ButtonListener implements View.OnClickListener {

        Intent intent;
        @Override
        public void onClick(View view) {
            //根据按钮切换游戏难度
            switch (view.getId()){
                case R.id.easy:
                    LevelSoundActivity.GAME_LEVEL = 0;
                    break;
                case R.id.medium:
                    LevelSoundActivity.GAME_LEVEL = 1;
                    break;
                case R.id.hard:
                    LevelSoundActivity.GAME_LEVEL = 2;
                    break;
            }
            intent = new Intent(LevelSoundActivity.this, GameActivity.class);
            //activity跳转
            startActivity(intent);
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