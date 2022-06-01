package com.hit.sz.item.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class RecordRepository {

    private RecordDao mRecordDao;
    private LiveData<List<MyRecord>> mAllRecords;

    RecordRepository(Application application) {
        DataBase db = DataBase.getDatabase(application);
        mRecordDao = db.RecordDao();
        mAllRecords = mRecordDao.getAllRecordsByRanking();
    }

    LiveData<List<MyRecord>> getAllRecords() {
        return mAllRecords;
    }

    void insert(MyRecord MyRecord) {
        DataBase.databaseWriteExecutor.execute(() -> {
            mRecordDao.insert(MyRecord);
        });
    }
}
