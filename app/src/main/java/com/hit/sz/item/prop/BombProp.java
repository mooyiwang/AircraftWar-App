package com.hit.sz.item.prop;

import com.hit.sz.item.aircraft.EliteEnemy;
import com.hit.sz.item.aircraft.HeroAircraft;
import com.hit.sz.item.aircraft.MobEnemy;
import com.hit.sz.item.basic.AbstractFlyingObject;
import com.hit.sz.item.bullet.EnemyBullet;


import java.util.ArrayList;
import java.util.List;

public class BombProp extends AbstractProp{

    private List<AbstractFlyingObject> enemyAndEnemyBullet = new ArrayList<>();

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void addBombed(AbstractFlyingObject bombed){
        this.enemyAndEnemyBullet.add(bombed);
    }

    public void removeBombed(AbstractFlyingObject bombed){
        this.enemyAndEnemyBullet.remove(bombed);
    }

    public void notifyAllSub(){
        for(AbstractFlyingObject bombed : enemyAndEnemyBullet){
            if(bombed instanceof EnemyBullet){
                ((EnemyBullet) bombed).update();
            }
            else if(bombed instanceof EliteEnemy){
                ((EliteEnemy) bombed).update();
            }
            else{
                ((MobEnemy) bombed).update();
            }
        }
    }

    @Override
    public void function(HeroAircraft heroAircraft){
        notifyAllSub();
        System.out.println("BombSupply Active!");
    }
}
