package com.hit.sz.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.hit.sz.item.strategy.ShootStrategy;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class EasyGameView extends AbstractGameView{

    public EasyGameView(Context context){
        super(context);
        gameLevel = "Easy";
    }

    @Override
    public void setBackgroundImage() {

    }

    @Override
    public boolean hasBoss() {
        return false;
    }

    @Override
    public boolean isBossHpUp() {
        return false;
    }

    @Override
    public int setScoreThreshold() {
        return 100;
    }

    @Override
    public int setEnemyHp() {
        return 10;
    }

    @Override
    public void hardnessPrint(int timeCnt) {

    }

    @Override
    public ShootStrategy setBossStrategy() {
        return null;
    }

    @Override
    public int setEnemyBulletPower() {
        return 10;
    }

    @Override
    public boolean isCreateElite(int timeCnt) {
        if(timeCnt % 8 ==0){
            return true;
        }
        else{
            return false;
        }
    }




    //后面的不用写
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        super.surfaceChanged(surfaceHolder, format, width, height);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        super.surfaceDestroyed(surfaceHolder);
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
