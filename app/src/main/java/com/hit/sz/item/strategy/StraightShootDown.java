package com.hit.sz.item.strategy;



import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.bullet.BaseBullet;
import com.hit.sz.item.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class StraightShootDown extends ShootStrategy{

//    int shootNum = 1;     //子弹一次发射数量
//    int power = 15;       //子弹伤害
//    int direction = 1;  //子弹射击方向 (向上发射：1，向下发射：-1) //模板这里方向反了

    public StraightShootDown() {
        shootNum = 1;
        power = 15;
        direction = 1;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft){
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction*5;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }
}
