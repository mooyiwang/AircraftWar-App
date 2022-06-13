package com.hit.sz.webservice.web.execute;

import com.hit.sz.webservice.IOStream.MyObjectInputStream;
import com.hit.sz.webservice.data.CheckData;
import com.hit.sz.webservice.data.DataPackage;
import com.hit.sz.webservice.data.UserData;
import com.hit.sz.webservice.data.UserDataReq;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

public class GetUser implements Callable<UserData> {
    private String name;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;

    public GetUser(String name, MyObjectInputStream objIn, ObjectOutputStream objOut) {
        this.name = name;
        this.objIn = objIn;
        this.objOut = objOut;
    }


    @Override
    public UserData call() throws Exception {
        DataPackage getUserDataReq = new UserDataReq(7, name);

        objOut.writeObject(getUserDataReq);

        while(true){
            try {
                DataPackage dataPackage = (DataPackage) objIn.readObject();
                if(dataPackage.getType()==3){
                    UserData getData = (UserData) dataPackage;
                    return getData;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
