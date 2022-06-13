package com.hit.sz.webservice.data;

import java.io.Serializable;

public class CheckData extends DataPackage implements Serializable {
    private static final long serialVersionUID = 529201591143307494L;;
    private boolean checked;

    public CheckData(int type, boolean checked) {
        super(type);
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
