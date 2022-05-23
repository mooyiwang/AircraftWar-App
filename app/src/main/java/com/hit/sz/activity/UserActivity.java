package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hit.sz.R;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private Button standalone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        standalone = findViewById(R.id.standalone_button);
        standalone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.standalone_button){
            Intent intent = new Intent(UserActivity.this, LevelSoundActivity.class);
            startActivity(intent);
        }
    }
}