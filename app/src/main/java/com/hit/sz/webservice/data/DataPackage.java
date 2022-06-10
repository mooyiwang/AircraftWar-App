package com.hit.sz.webservice.data;

import java.io.Serializable;

public abstract class DataPackage implements Serializable {
    protected int type;
    private static final long serialVersionUID = 1L;
    public DataPackage(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
