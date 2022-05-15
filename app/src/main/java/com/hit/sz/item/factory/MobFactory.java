package com.hit.sz.item.factory;

import com.hit.sz.application.ImageManager;
import com.hit.sz.application.MainActivity;
import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.aircraft.MobEnemy;
import com.hit.sz.item.strategy.ShootNothing;


public class MobFactory implements EnemyFactory {

    @Override
    public AbstractAircraft enemyCreator(){
        return new MobEnemy((int) ( Math.random() * (MainActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                            0,
                            0,
                            10,
                            30,
                            new ShootNothing());
    }
}

