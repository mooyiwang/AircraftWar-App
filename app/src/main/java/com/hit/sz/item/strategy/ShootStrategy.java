package com.hit.sz.item.strategy;

import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.bullet.BaseBullet;



import java.util.List;

public abstract class ShootStrategy {
    /**
     * shootNum 子弹一次发射数量
     * power 子弹伤害
     * direction 子弹射击方向 (向上发射：1，向下发射：-1) //模板这里方向反了
     */
    protected int shootNum;
    protected int power;
    protected int direction;

    public ShootStrategy(){
        this.shootNum = 0;
        this.power = 0;
        this.direction = 0;
    }

    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public abstract List<BaseBullet> shoot(AbstractAircraft aircraft);
}
