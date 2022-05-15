package com.hit.sz.item.aircraft;


import com.hit.sz.application.MainActivity;
import com.hit.sz.item.strategy.ShootStrategy;
import com.hit.sz.view.AbstractGameView;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft implements BombPropActivateUpdate {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy strategy) {
        super(locationX, locationY, speedX, speedY, hp, strategy);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.screenHeight) {
            vanish();
        }
    }

//    @Override
//    public List<BaseBullet> shoot() {
//        return shootStrategy.shoot(this);
//    }

    @Override
    public void update() {
        this.vanish();
        AbstractGameView.score += 30;
    }

}
