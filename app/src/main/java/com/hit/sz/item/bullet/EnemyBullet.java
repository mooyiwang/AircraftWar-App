package com.hit.sz.item.bullet;


import com.hit.sz.item.aircraft.BombPropActivateUpdate;

/**
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements BombPropActivateUpdate {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public void update() {
        this.vanish();
    }

}
