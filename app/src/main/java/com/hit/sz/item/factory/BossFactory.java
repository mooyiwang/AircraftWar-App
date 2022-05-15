package com.hit.sz.item.factory;


import com.hit.sz.application.ImageManager;
import com.hit.sz.application.MainActivity;
import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.aircraft.BossEnemy;
import com.hit.sz.item.strategy.DiffuseShootDown3;

public class BossFactory implements EnemyFactory {

    @Override
    public AbstractAircraft enemyCreator(){
        return new BossEnemy(
                (int)(Math.random() * (MainActivity.screenWidth - ImageManager.BOSS_ENEMY_IMAGE.getWidth()))*1,
                5,
                2,
                0,
                300,
                new DiffuseShootDown3());
    }

}
