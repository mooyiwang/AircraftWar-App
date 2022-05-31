package com.hit.sz.item.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(MyRecord myRecord);

    @Query("DELETE FROM record_table")
    void deleteAll();

    @Query("SELECT * FROM record_table ORDER BY score ASC")
    LiveData<List<MyRecord>> getAllRecordsByRanking();
}