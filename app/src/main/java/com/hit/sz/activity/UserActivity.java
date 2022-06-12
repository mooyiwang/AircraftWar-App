package com.hit.sz.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.hit.sz.R;

import java.util.Arrays;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private int [] nums = new int[5];

    private SharedPreferences userPoints;
    private String userName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Button standalone = findViewById(R.id.standalone_button);
        standalone.setOnClickListener(this);
        Button propStore  = findViewById(R.id.propstore_button);
        propStore.setOnClickListener(this);
        //读取数据
        userName = "tourists";
        String sharedPrefFile = "com.hit.sz.userpoints" + "." + userName;
        userPoints =getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        for (int i = 0 ;i< 5;i++){
            nums[i] = userPoints.getInt("num "+i, i==4?10:0);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.standalone_button){
            Intent intent = new Intent(UserActivity.this, LevelSoundActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.propstore_button){
            Intent intent = new Intent(this, PropertyStoreActivity.class);
            intent.putExtra("nums",nums);
            someActivityResultLauncher.launch(intent);
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

}