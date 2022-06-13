package com.hit.sz.webservice.web.execute;

import com.hit.sz.webservice.IOStream.MyObjectInputStream;
import com.hit.sz.webservice.data.CheckData;
import com.hit.sz.webservice.data.DataPackage;
import com.hit.sz.webservice.data.NameCheckData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

public class NameCheck implements Callable<Boolean> {
    private String name;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;

    public NameCheck(String name, MyObjectInputStream objIn, ObjectOutputStream objOut) {
        this.name = name;
        this.objIn = objIn;
        this.objOut = objOut;
    }


    @Override
    public Boolean call() throws Exception {
        DataPackage sendData = new NameCheckData(6, name);
        objOut.writeObject(sendData);
        while(true){
            try {
                DataPackage dataPackage = (DataPackage) objIn.readObject();
                if(dataPackage.getType()==2){
                    CheckData checkResult = (CheckData)dataPackage;
                    return checkResult.isChecked();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
