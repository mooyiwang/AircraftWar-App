package com.hit.sz.webservice.data;

public class SignupData extends DataPackage{
    private String name;
    private String pwd;


    public SignupData(int type, String name, String pwd) {
        super(type);
        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
