package com.hit.sz.webservice.data;

import java.io.Serializable;

public class BattleReq extends DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;;
    private boolean request;

    public BattleReq(int type, boolean request) {
        super(type);
        this.request = request;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }
}
