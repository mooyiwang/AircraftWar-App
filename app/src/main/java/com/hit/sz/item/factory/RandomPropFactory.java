package com.hit.sz.item.factory;


import com.hit.sz.item.prop.AbstractProp;
import com.hit.sz.item.prop.BloodProp;
import com.hit.sz.item.prop.BombProp;
import com.hit.sz.item.prop.FireProp;

public class RandomPropFactory implements PropFactory {
    @Override
    public AbstractProp randomPropCreator(int locationX, int locationY) {
        switch ((int) (Math.random() * 4 + 1) * 1) {
            case 1:
                return new BloodProp(
                        locationX,
                        locationY,
                        0,
                        2);
            case 2:
                return new BombProp(
                        locationX,
                        locationY,
                        0,
                        2);
            case 3:
                return new FireProp(
                        locationX,
                        locationY,
                        0,
                        2);
            default:
                return null;
        }
    }
}

