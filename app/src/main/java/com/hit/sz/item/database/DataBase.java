package com.hit.sz.item.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {MyRecord.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    public abstract RecordDao RecordDao();

    private static volatile DataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                RecordDao dao = INSTANCE.RecordDao();
                dao.deleteAll();

                MyRecord record = new MyRecord("Hello","hello",123,"easy");
                dao.insert(record);
                record = new MyRecord("World","word!!!",123,"easy");
                dao.insert(record);
            });
        }
    };
    static DataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DataBase.class, "main_database")
                            .addCallback(sRoomDatabaseCallback).build();//.build();
                }
            }
        }
        return INSTANCE;
    }


}