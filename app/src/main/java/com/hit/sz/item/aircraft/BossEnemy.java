package com.hit.sz.item.aircraft;

import com.hit.sz.activity.LevelSoundActivity;
import com.hit.sz.item.strategy.ShootStrategy;



public class BossEnemy extends AbstractAircraft {

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy strategy) {
        super(locationX, locationY, speedX, speedY, hp, strategy);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= LevelSoundActivity.screenHeight) {
            vanish();
        }
    }
}
