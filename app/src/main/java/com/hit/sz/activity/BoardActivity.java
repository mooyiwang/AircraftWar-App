package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;

import com.hit.sz.R;
import com.hit.sz.item.database.BoardViewModel;
import com.hit.sz.item.database.MyRecord;
import com.hit.sz.item.database.MyViewModelFactory;
import com.hit.sz.item.database.RecordListAdapter;

import java.util.Objects;


public class BoardActivity extends AppCompatActivity {
    private BoardViewModel boardViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GameActivity.instance.finish();

        Intent intent = getIntent();
        switch (intent.getStringExtra("level")) {
            case "medium":
                setTitle("普通模式排行榜");
                break;
            case "easy":
                setTitle("简单模式排行榜");
                break;
            case "hard":
                setTitle("困难模式排行榜");
                break;
            default:
                setTitle("排行榜");
        }


        setContentView(R.layout.activity_board);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RecordListAdapter adapter = new RecordListAdapter(new RecordListAdapter.RecordDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        boardViewModel = new ViewModelProvider(
                this,
                new MyViewModelFactory( this.getApplication(), intent.getStringExtra("level"))
        ).get(BoardViewModel.class);

        BoardViewModel.getAllRecords().observe(this, adapter::submitList);
        //添加当次记录
        MyRecord thisRecord = new MyRecord(
                intent.getStringExtra("date"),
                intent.getStringExtra("name"),
                intent.getIntExtra("score",0),
                intent.getStringExtra("level")
                );
        boardViewModel.insert(thisRecord);
        Button button = findViewById(R.id.addBoardButton);
        button.setOnClickListener( view -> {
            MyRecord myRecord = new MyRecord("2023.6.2","pppp",1234,"medium");
            boardViewModel.insert(myRecord);
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return true;
    }

}
