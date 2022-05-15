package com.hit.sz.item.prop;

import com.hit.sz.application.MainActivity;
import com.hit.sz.item.aircraft.HeroAircraft;
import com.hit.sz.item.basic.AbstractFlyingObject;



public abstract class AbstractProp extends AbstractFlyingObject {

    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * @param heroAircraft 英雄机
     */
    public abstract void function(HeroAircraft heroAircraft);

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.screenHeight ) {
            vanish();
        }
    }

}
