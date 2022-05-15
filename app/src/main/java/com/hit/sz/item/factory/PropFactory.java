package com.hit.sz.item.factory;


import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.prop.AbstractProp;

public interface PropFactory {
    /**
     *
     * @param locationX 击落敌机的位置X
     * @param locationY 击落敌机的位置Y
     * @return 创建的道具
     */
    AbstractProp randomPropCreator(int locationX, int locationY);
}

