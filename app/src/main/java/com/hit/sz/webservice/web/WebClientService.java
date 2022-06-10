package com.hit.sz.webservice.web;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hit.sz.webservice.IOStream.MyObjectInputStream;
import com.hit.sz.webservice.data.CheckData;
import com.hit.sz.webservice.data.DataPackage;
import com.hit.sz.webservice.data.LoginData;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * DataPackage Type
 * loginData 0
 * SignupData 1
 * CheckData 2
 * UserData 3
 * BattleData 5
 */
public class WebClientService extends Service {
    private static final String TAG = "WebClientService";
    private boolean connecterState;
    private InputStream in;
    private OutputStream out;
    private MyObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private Handler handler;
    private DataPackage data;



    private Socket socket;
    public WebClientService(){}


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("serv","try to1");
        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){

                }
            }
        };
        new Thread(new NetConn()).start();
    }

    class NetConn extends Thread{


        public NetConn(){
        }

        @Override
        public void run() {
            socket = new Socket();
            try {
                socket.connect(new InetSocketAddress
                        ("192.168.56.1",9999),1000);
                Log.i("serv","try to2");
                in = socket.getInputStream();
                out = socket.getOutputStream();
                //System.out.println(out);
                Log.i("Client","connect to server");
                objOut = new ObjectOutputStream(out);
                objIn = new MyObjectInputStream(in);
                System.out.println(objOut);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public IBinder onBind(Intent intent) {
        return new WebBinder();
    }

    public class WebBinder extends Binder {

        public boolean signupNameCheck(String name){
            return true;
        }

        public boolean loginVerify(String name, String pwd){
            FutureTask<Boolean> task = new FutureTask<Boolean>(new LoginVerify(name, pwd));
            new Thread(task).start();
            boolean isSucc = false;
            try {
                isSucc = task.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return isSucc;
        }

        public boolean signup(String name, String pwd){return true;}

    }

    class LoginVerify implements Callable<Boolean>{

        private String name;
        private String pwd;
        public LoginVerify(String name, String pwd){
            this.name = name;
            this.pwd = pwd;
        }
        @Override
        public Boolean call() throws Exception {
            data = new LoginData(0, name, pwd);
            System.out.println(objOut);
            try {
                objOut.writeObject(data);
                System.out.println("555556666");
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    DataPackage dataPackage = (DataPackage) objIn.readObject();
                    if(dataPackage.getType()==2){
                        System.out.println("5555566667777777");
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


}
