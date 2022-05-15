package com.hit.sz.item.factory;

import com.hit.sz.application.ImageManager;
import com.hit.sz.application.MainActivity;
import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.aircraft.EliteEnemy;
import com.hit.sz.item.strategy.StraightShootDown;


public class EliteFactory implements EnemyFactory {


    @Override
    public AbstractAircraft enemyCreator(){
        return new EliteEnemy(
                            (int)(Math.random() * (MainActivity.screenWidth - ImageManager.ELITE_ENEMY_IMAGE.getWidth()))*1,
                            0,
                            (int)(Math.random()*( 5 - (-5) + 1) + (-5) )*1,
                            7,
                            30,
                            new StraightShootDown());
    }
}
