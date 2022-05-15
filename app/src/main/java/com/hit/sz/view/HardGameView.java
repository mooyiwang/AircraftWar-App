package com.hit.sz.view;

import android.content.Context;

import com.hit.sz.item.strategy.ShootStrategy;

public class HardGameView extends AbstractGameView{

    public HardGameView(Context context) {
        super(context);
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
        return 0;
    }

    @Override
    public int setEnemyHp() {
        return 0;
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
        return 0;
    }

    @Override
    public boolean isCreateElite(int timeCnt) {
        return false;
    }
}
