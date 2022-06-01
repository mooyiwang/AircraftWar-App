package com.hit.sz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.hit.sz.R;
import com.hit.sz.item.database.BoardViewModel;
import com.hit.sz.item.database.RecordListAdapter;


public class BoardActivity extends AppCompatActivity {
    private BoardViewModel boardViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.ranklist);
        setContentView(R.layout.activity_board);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RecordListAdapter adapter = new RecordListAdapter(new RecordListAdapter.RecordDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        boardViewModel = new ViewModelProvider(this).get(BoardViewModel.class);

        BoardViewModel.getAllRecords().observe(this, records -> {
            // Update the cached copy of the words in the adapter.
            adapter.submitList(records);
        });

    }

}
