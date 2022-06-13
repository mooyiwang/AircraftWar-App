package com.hit.sz.webservice.data;

import java.io.Serializable;
import java.util.HashMap;

public class UserData extends DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;
    private String name;
    private String password;
    private int bonus;
    private int level;
    private HashMap<String, Integer> broughtProps;

    public UserData(int type, String name, String password, int bonus, int level) {


        super(type);
        this.name = name;
        this.password = password;
        this.bonus = bonus;
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
