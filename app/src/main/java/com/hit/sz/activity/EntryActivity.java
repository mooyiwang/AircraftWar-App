package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hit.sz.R;

public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private Button signup;
    private Button visitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        visitor = findViewById(R.id.visitor);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        visitor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                setContentView(R.layout.entry_login);
                break;
            case R.id.signup:
                setContentView(R.layout.entry_signup);
                break;
            case R.id.visitor:
                Intent intent = new Intent(EntryActivity.this, LevelSoundActivity.class);
                startActivity(intent);
                break;
        }
    }
}