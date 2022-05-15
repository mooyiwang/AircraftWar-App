package com.hit.sz.item.prop;

import com.hit.sz.item.aircraft.HeroAircraft;
import com.hit.sz.item.strategy.DiffuseShootUp3;
import com.hit.sz.item.strategy.DiffuseShootUp5;
import com.hit.sz.item.strategy.StraightShootUp;



public class FireProp extends AbstractProp{

    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void function(HeroAircraft heroAircraft){
        Runnable r = () -> {
            if(heroAircraft.getStrategy() instanceof DiffuseShootUp3 || heroAircraft.getStrategy() instanceof DiffuseShootUp5 ){
                heroAircraft.setStrategy(new DiffuseShootUp5());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                heroAircraft.setStrategy(new StraightShootUp());
            }
            else {
                heroAircraft.setStrategy(new DiffuseShootUp3());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                heroAircraft.setStrategy(new StraightShootUp());
            }
        };
        System.out.println("FireSupply Active!");
        new Thread(r).start();
    };
}
