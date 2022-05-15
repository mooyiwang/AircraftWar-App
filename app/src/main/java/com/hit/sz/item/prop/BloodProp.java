package com.hit.sz.item.prop;

import com.hit.sz.item.aircraft.HeroAircraft;



public class BloodProp extends AbstractProp{

    private static int increase = 50;

    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void function(HeroAircraft heroAircraft){
        heroAircraft.increaseHp(increase);
    };
}
