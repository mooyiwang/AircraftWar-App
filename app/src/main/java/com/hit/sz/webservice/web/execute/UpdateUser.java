package com.hit.sz.webservice.web.execute;

import com.hit.sz.webservice.IOStream.MyObjectInputStream;
import com.hit.sz.webservice.data.DataPackage;
import com.hit.sz.webservice.data.UpdateUserData;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class UpdateUser extends Thread {

    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private String name;
    private int bonus;

    public UpdateUser(MyObjectInputStream objIn, ObjectOutputStream objOut, String name, int bonus) {
        this.objIn = objIn;
        this.objOut = objOut;
        this.name = name;
        this.bonus = bonus;
    }

    @Override
    public void run(){
        DataPackage updateData = new UpdateUserData(8, name, bonus);
        try {
            objOut.writeObject(updateData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
