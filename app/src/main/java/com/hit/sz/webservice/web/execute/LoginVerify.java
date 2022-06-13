package com.hit.sz.webservice.web.execute;

import com.hit.sz.webservice.IOStream.MyObjectInputStream;
import com.hit.sz.webservice.data.CheckData;
import com.hit.sz.webservice.data.DataPackage;
import com.hit.sz.webservice.data.LoginData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

public class LoginVerify implements Callable<Boolean> {

    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private String name;
    private String pwd;
    public LoginVerify(String name, String pwd, MyObjectInputStream objIn, ObjectOutputStream objOut){
        this.name = name;
        this.pwd = pwd;
        this.objIn = objIn;
        this.objOut = objOut;
    }
    @Override
    public Boolean call() throws Exception {
        DataPackage sendData = new LoginData(0, name, pwd);
//        System.out.println(objOut);
        try {
            objOut.writeObject(sendData);
//                System.out.println("555556666");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                DataPackage dataPackage = (DataPackage) objIn.readObject();
                if(dataPackage.getType()==2){
//                        System.out.println("5555566667777777");
                    CheckData loginResult = (CheckData)dataPackage;
                    return loginResult.isChecked();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
