package com.hit.sz.webservice.data;

public class CheckData extends DataPackage{
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
