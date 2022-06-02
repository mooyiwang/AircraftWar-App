package com.hit.sz.item.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BoardViewModel extends AndroidViewModel {
    private RecordRepository mRepository;
    private static LiveData<List<MyRecord>> mAllRecords;
    private String lvl;
    public BoardViewModel ( Application application) {
        super(application);
        mRepository = new RecordRepository(application);
        mAllRecords = mRepository.getAllRecords();
    }
    public BoardViewModel ( Application application, String lvl) {
        super(application);
        mRepository = new RecordRepository(application,lvl);
        this.lvl = lvl;
        mAllRecords = mRepository.getAllRecords();
    }

    public static LiveData<List<MyRecord>> getAllRecords() { return mAllRecords; }

    public void insert(MyRecord MyRecord) { mRepository.insert(MyRecord); }
}