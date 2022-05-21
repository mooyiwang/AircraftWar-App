package com.hit.sz.item.aircraft;


import com.hit.sz.activity.LevelSoundActivity;
import com.hit.sz.item.strategy.ShootStrategy;
import com.hit.sz.view.AbstractGameView;

/**
 * 精英敌机
 * 向下发射敌机子弹EnemyBullet
 *
 * @author hitsz.WangMuyi
 */
public class EliteEnemy extends AbstractAircraft implements BombPropActivateUpdate {


    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy strategy) {
        super(locationX, locationY, speedX, speedY, hp, strategy);
    }

    @Override
    public void forward(){
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= LevelSoundActivity.screenHeight) {
            vanish();
        }
    }

    @Override
    public void update() {
        this.vanish();
        AbstractGameView.score += 30;
    }

}
