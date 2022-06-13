package com.hit.sz.webservice.data;

import java.io.Serializable;

public class NameCheckData extends DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;
    private String name;

    public NameCheckData(int type, String name) {
        super(type);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
