package com.hit.sz.item.factory;

import com.hit.sz.item.aircraft.AbstractAircraft;



public interface EnemyFactory {
    /**
     *
     * @return 创建好的敌机
     */
    AbstractAircraft enemyCreator();
}
