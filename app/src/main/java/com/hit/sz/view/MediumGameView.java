package com.hit.sz.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

import com.hit.sz.R;
import com.hit.sz.application.ImageManager;
import com.hit.sz.item.strategy.DiffuseShootDown3;
import com.hit.sz.item.strategy.ShootStrategy;

public class MediumGameView extends AbstractGameView{
    public MediumGameView(Context context) {
        super(context);
        shootInterval = 5;

    }

    @Override
    public void setBackgroundImage() {
        ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
    }

    @Override
    public boolean hasBoss() {
        return true;
    }

    @Override
    public boolean isBossHpUp() {
        return false;
    }

    @Override
    public int setScoreThreshold() {
        return 500;
    }

    @Override
    public int setEnemyHp() {
        return 30;
    }

    @Override
    public void hardnessPrint(int timeCnt) {

    }

    @Override
    public ShootStrategy setBossStrategy() {
        return new DiffuseShootDown3();
    }

    @Override
    public int setEnemyBulletPower() {
        return 10;
    }

    @Override
    public boolean isCreateElite(int timeCnt) {
        return true;
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
