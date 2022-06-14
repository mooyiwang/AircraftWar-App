package com.hit.sz.webservice.web;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hit.sz.activity.BoardActivity;
import com.hit.sz.view.AbstractGameView;
import com.hit.sz.webservice.IOStream.MyObjectInputStream;
import com.hit.sz.webservice.data.DataPackage;
import com.hit.sz.webservice.data.UserData;
import com.hit.sz.webservice.web.execute.GetUser;
import com.hit.sz.webservice.web.execute.LoginVerify;
import com.hit.sz.webservice.web.execute.NameCheck;
import com.hit.sz.webservice.web.execute.PlayerCommu;
import com.hit.sz.webservice.web.execute.PlayerMatch;
import com.hit.sz.webservice.web.execute.Signup;
import com.hit.sz.webservice.web.execute.UpdateUser;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * DataPackage Type
 * loginData 0
 * SignupData 1
 * CheckData 2
 * UserData 3
 * BattleData 5
 * NameCheck 6
 * GetUserData 7
 * UpdateUser 8
 * BattleReq 9
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

        /**
         * 注册时重名检测
         * @param name
         * @return
         */
        public boolean signupNameCheck(String name){
            FutureTask<Boolean> task = new FutureTask<>(new NameCheck(name, objIn, objOut));
            new Thread(task).start();
            boolean notSame = true;
            try {
                notSame = task.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return notSame;
        }

        /**
         * 登录校验
         * @param name
         * @param pwd
         * @return
         */
        public boolean loginVerify(String name, String pwd){
            FutureTask<Boolean> task = new FutureTask<Boolean>(new LoginVerify(name, pwd, objIn, objOut));
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

        /**
         * 注册操作
         * @param name
         * @param pwd
         * @return
         */
        public boolean signup(String name, String pwd){
            FutureTask<Boolean> task = new FutureTask<>(new Signup(name, pwd, objIn,objOut));
            new Thread(task).start();
            boolean isSucc = true;
            try {
                isSucc = task.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return isSucc;
        }

        public UserData getUserData(String name){
            FutureTask<UserData> task = new FutureTask<>(new GetUser(name, objIn, objOut));
            new Thread(task).start();
            UserData userData = null;
            try {
                Thread.sleep(500);
                userData = task.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return userData;
        }


        public void updateUserData(String name, int bonus){
            new Thread(new UpdateUser(objIn, objOut, name, bonus)).start();
        }

        public void playerMatch(Handler handler){
            new Thread(new PlayerMatch(objIn, objOut, handler)).start();
        }

        public void playerCommu(AbstractGameView gameView){
            new Thread(new PlayerCommu(objIn, objOut, gameView)).start();
        }
    }
}
