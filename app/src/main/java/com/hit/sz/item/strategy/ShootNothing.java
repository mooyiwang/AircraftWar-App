package com.hit.sz.item.strategy;

import com.hit.sz.item.aircraft.AbstractAircraft;
import com.hit.sz.item.bullet.BaseBullet;



import java.util.LinkedList;
import java.util.List;

public class ShootNothing extends ShootStrategy{


    public ShootNothing() {
        super();
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft){
        return new LinkedList<>();
    }
}

