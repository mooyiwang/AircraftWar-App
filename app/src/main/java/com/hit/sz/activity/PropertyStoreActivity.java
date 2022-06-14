package com.hit.sz.activity;


import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.hit.sz.R;

public class PropertyStoreActivity extends AppCompatActivity  implements View.OnClickListener  {

    private  int maxPoint,currentPoint;
    TextView bloodText,bulletText,speedText,atkText,totText;
    private  int[] nums = new int[5];
    private final int bld =0, atk = 1, spd = 2, blt= 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_store);
        //读取数据
        Intent intent = this.getIntent();
        nums = intent.getIntArrayExtra("nums");
        currentPoint = nums[0]+nums[1]+nums[2]+nums[3];
        maxPoint = nums[4];
        //绑定相关元素
        bloodText = findViewById(R.id.blood_num);
        bulletText = findViewById(R.id.bullet_num);
        speedText = findViewById(R.id.speed_num);
        atkText = findViewById(R.id.atack_num);
        totText = findViewById(R.id.total_point);
        ImageButton bt;
        bt = findViewById(R.id.blood_down);
        bt.setOnClickListener(this);
        bt = findViewById(R.id.blood_up);
        bt.setOnClickListener(this);
        bt = findViewById(R.id.bullet_down);
        bt.setOnClickListener(this);
        bt = findViewById(R.id.bullet_up);
        bt.setOnClickListener(this);
        bt = findViewById(R.id.speed_down);
        bt.setOnClickListener(this);
        bt = findViewById(R.id.speed_up);
        bt.setOnClickListener(this);
        bt = findViewById(R.id.atack_down);
        bt.setOnClickListener(this);
        bt = findViewById(R.id.attack_up);
        bt.setOnClickListener(this);
        bt = findViewById(R.id.save_button);
        bt.setOnClickListener(this);
        updateText();
    }
    @SuppressLint("SetTextI18n")
    protected  void updateText(){
        if(bloodText!=null) bloodText.setText("+" + Integer.toString(10 * nums[bld]) + "%");
        if(bulletText!=null) bulletText.setText("+" + Integer.toString(nums[blt]/5) + "颗");
        if(speedText!=null) speedText.setText("+" + Integer.toString(10 * nums[spd]) + "%");
        if(atkText!=null) atkText.setText("+" + Integer.toString(10 * nums[atk]) + "%");
        if(totText!=null) totText.setText(Integer.toString(maxPoint-currentPoint));
    }

    public void SaveChange(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert = builder
                .setTitle("提示：")
                .setMessage("确定保存当前加点？\n（之后可以随时更换）")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent replyIntent = new Intent();
                        replyIntent.putExtra("nums", nums);
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    }
                }).create();             //创建AlertDialog对象
        alert.show();                    //显示对话框
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.save_button){
            SaveChange();
            return;
        }

        String hint = "null";
        int ty1,ty2;
        switch (view.getId()){
            case R.id.blood_down:
                hint = "降低生命点数";
                ty1 = -1;
                ty2 = bld;
                break;
            case R.id.blood_up:
                hint = "增加生命点数";
                ty1 = 1;
                ty2 = bld;
                break;
            case R.id.bullet_down:
                hint = "降低子弹数量点数";
                ty1 = -1;
                ty2 = blt;
                break;
            case R.id.bullet_up:
                hint = "增加子弹数量点数";
                ty1 = 1;
                ty2 = blt;
                break;
            case R.id.speed_up:
                hint = "增加射速点数";
                ty1 = 1;
                ty2 = spd;
                break;
            case R.id.speed_down:
                hint = "减少射速点数";
                ty1 = -1;
                ty2 = spd;
                break;
            case R.id.atack_down:
                hint = "减少攻击点数";
                ty1 = -1;
                ty2 = atk;
                break;
            case R.id.attack_up:
                hint = "增加攻击点数";
                ty1 = 1;
                ty2 = atk;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
        if(ty1 + currentPoint > maxPoint){
            Snackbar.make(view, "没有可以分配的点数！", BaseTransientBottomBar.LENGTH_SHORT).show();
        }else if(ty1 + nums[ty2]< 0){
            Snackbar.make(view, "不能再降低了！", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
        else {
            Snackbar.make(view, hint, BaseTransientBottomBar.LENGTH_SHORT).show();
            nums[ty2] +=ty1;
            currentPoint +=ty1;
            updateText();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return true;
    }
}