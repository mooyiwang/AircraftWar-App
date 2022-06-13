package com.hit.sz.webservice.web.execute;

import com.hit.sz.webservice.IOStream.MyObjectInputStream;
import com.hit.sz.webservice.data.CheckData;
import com.hit.sz.webservice.data.DataPackage;
import com.hit.sz.webservice.data.SignupData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

public class Signup implements Callable<Boolean> {
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private String name;
    private String pwd;

    public Signup(String name, String pwd, MyObjectInputStream objIn, ObjectOutputStream objOut){
        this.name = name;
        this.pwd = pwd;
        this.objIn = objIn;
        this.objOut = objOut;
    }
    @Override
    public Boolean call() throws Exception {
        DataPackage sendData = new SignupData(1, name, pwd);
        objOut.writeObject(sendData);

        return true;
    }
}
