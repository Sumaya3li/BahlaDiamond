package com.databoat.barcodescanner.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Previous.class, Current.class}, version = 17, exportSchema = false)
public abstract class DatabaseHelper extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract PreviousDao previousDao();
    public abstract CurrentDao currentDao();

    private static volatile DatabaseHelper INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static DatabaseHelper getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseHelper.class, "bahla_diamond")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
