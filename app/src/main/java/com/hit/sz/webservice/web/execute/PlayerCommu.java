package com.hit.sz.webservice.web.execute;

import com.hit.sz.view.AbstractGameView;
import com.hit.sz.webservice.IOStream.MyObjectInputStream;
import com.hit.sz.webservice.data.BattleData;
import com.hit.sz.webservice.data.DataPackage;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class PlayerCommu extends Thread{
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private AbstractGameView gameView;

    public PlayerCommu(MyObjectInputStream objIn, ObjectOutputStream objOut, AbstractGameView gameView) {
        this.objIn = objIn;
        this.objOut = objOut;
        this.gameView = gameView;
    }

    @Override
    public void run() {
        while(true){
            DataPackage sendData = new BattleData(5, true, true, AbstractGameView.score, gameView.getLife());
            try {
                objOut.writeObject(sendData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(true){
                try {
                    DataPackage enemyData = (DataPackage) objIn.readObject();
                    if(enemyData.getType()==5){
                        BattleData enemy = (BattleData) enemyData;
                        if(enemy.getCurLife()<=0){
                            gameView.updateOnlinePlayer(enemy.getCurScore(), enemy.getCurLife(), true);
                        }
                        gameView.updateOnlinePlayer(enemy.getCurScore(), enemy.getCurLife(), false);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
