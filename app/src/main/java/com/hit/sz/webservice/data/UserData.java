package com.hit.sz.webservice.data;

import java.io.Serializable;
import java.util.HashMap;

public class UserData extends DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;
    private String name;
    private String password;
    private int bonus;


    public UserData(int type, String name, String password, int bonus) {


        super(type);
        this.name = name;
        this.password = password;
        this.bonus = bonus;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

}
