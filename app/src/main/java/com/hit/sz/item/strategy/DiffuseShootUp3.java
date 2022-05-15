package com.hit.sz.item.strategy;



import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.bullet.BaseBullet;
import com.hit.sz.item.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class DiffuseShootUp3 extends ShootStrategy{

    public DiffuseShootUp3() {
        shootNum = 3;
        power = 30;
        direction = -1;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft){
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction*2;
        int speedX = -1;
        int speedY = aircraft.getSpeedY() + direction*5;
        BaseBullet bullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            bullet = new HeroBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(bullet);
            speedX += 1;
        }
        return res;
    }
}
