package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.hit.sz.R;
import com.hit.sz.application.MainActivity;
import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.aircraft.HeroAircraft;
import com.hit.sz.view.AbstractGameView;
import com.hit.sz.view.EasyGameView;
import com.hit.sz.view.HardGameView;
import com.hit.sz.view.MediumGameView;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        switch (MainActivity.GAME_LEVEL){
            case 0:
                setContentView(new EasyGameView(this));
                break;
            case 1:
                setContentView(new MediumGameView(this));
                break;
            case 2:
                setContentView(new HardGameView(this));
        }
    }



}