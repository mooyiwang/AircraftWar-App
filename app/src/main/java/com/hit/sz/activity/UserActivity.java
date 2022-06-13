package com.hit.sz.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.hit.sz.R;
import com.hit.sz.webservice.data.DataPackage;
import com.hit.sz.webservice.data.UserData;
import com.hit.sz.webservice.web.WebClientService;

import java.util.Arrays;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private int [] nums = new int[5];


    private SharedPreferences userPoints;
    private String userName = null;

    //当前用户的点数
    private int cur_points = 10;


    private boolean isVisitor;

    private Button btnStandalone;
    private Button btnOnline;
    private Button btnPropStore;
    private Button btnLogout;
    private TextView txtName;
    private TextView txtPoint;

    private WebClientService.WebBinder webBinder;
    private WebClientServConn conn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        btnStandalone = findViewById(R.id.standalone_button);
        btnStandalone.setOnClickListener(this);
        btnPropStore  = findViewById(R.id.propstore_button);
        btnPropStore.setOnClickListener(this);
        btnOnline = findViewById(R.id.online_button);
        btnOnline.setOnClickListener(this);
        btnLogout = findViewById(R.id.logout_button);
        btnLogout.setOnClickListener(this);
        txtName = findViewById(R.id.name_display);
        txtPoint = findViewById(R.id.point_display);




        Intent intent2 = getIntent();
        Bundle extras = intent2.getExtras();
        isVisitor = extras.getBoolean("isVisitor");

        conn = new WebClientServConn();
        Intent intent1 = new Intent(UserActivity.this, WebClientService.class);
        this.bindService(intent1, conn, Context.BIND_AUTO_CREATE);




        String sharedPrefFile = "com.hit.sz.userpoints" + "." + userName;
        userPoints =getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        for (int i = 0 ;i< 5;i++){
            nums[i] = userPoints.getInt("num "+i, i==4?10:0);
        }

        if(isVisitor){
            userName = "tourist";
            txtName.setText("用户名：游客");
            //TODO 游客显示点数，按照sharedPref里存的改
            txtPoint.setText("当前点数：0");
        }
        else{
            cur_points = extras.getInt("userPoint");
            userName = extras.getString("userName");
            txtName.setText("用户名："+userName);
            txtPoint.setText("当前点数："+cur_points);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.standalone_button:
                Intent intent3 = new Intent(UserActivity.this, LevelSoundActivity.class);
                startActivity(intent3);
                break;
            case R.id.propstore_button:
                Intent intent2 = new Intent(UserActivity.this, PropertyStoreActivity.class);
                intent2.putExtra("nums",nums);
                someActivityResultLauncher.launch(intent2);
                break;
            case R.id.online_button:
                Intent intent4 = new Intent(UserActivity.this, WaitingActivity.class);
                startActivity(intent4);
                break;
            case R.id.logout_button:
                if(isVisitor == false){
                    webBinder.updateUserData(userName, cur_points);
                }
                finish();
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();

                    nums = data.getIntArrayExtra("nums");
                    if(nums!=null) System.out.println("OK here");
                    System.out.println(Arrays.toString(nums));
                    SharedPreferences.Editor editor = userPoints.edit();
                    for (int i = 0 ;i< 5;i++) editor.putInt("num "+i, nums[i]);
                    editor.apply();
                }
            });


    class WebClientServConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            webBinder = (WebClientService.WebBinder)iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

}