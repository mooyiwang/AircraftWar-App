package com.hit.sz.webservice.data;

import java.io.Serializable;

public class UpdateUserData extends DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;
    private String name;
    private int bonus;

    public UpdateUserData(int type, String name, int bonus) {
        super(type);
        this.name = name;
        this.bonus = bonus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
