package com.hit.sz.webservice.web.execute;

import android.os.Handler;
import android.os.Message;

import com.hit.sz.webservice.IOStream.MyObjectInputStream;
import com.hit.sz.webservice.data.BattleReq;
import com.hit.sz.webservice.data.CheckData;
import com.hit.sz.webservice.data.DataPackage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class PlayerMatch extends Thread{
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private Handler handler;

    public PlayerMatch(MyObjectInputStream objIn, ObjectOutputStream objOut, Handler handler) {
        this.objIn = objIn;
        this.objOut = objOut;
        this.handler = handler;
    }

    @Override
    public void run() {
        DataPackage sendData = new BattleReq(9, true);
        boolean isMatched = false;
        try {
            objOut.writeObject(sendData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                DataPackage dataPackage = (DataPackage) objIn.readObject();
                if(dataPackage.getType()==2){
                    CheckData result = (CheckData)dataPackage;
                    isMatched = result.isChecked();
                    break;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(isMatched){
            Message msg = new Message();
            msg.what = 1;
            msg.obj = true;
            handler.sendMessage(msg);
        }

    }
}
