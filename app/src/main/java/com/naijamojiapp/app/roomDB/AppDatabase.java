package com.naijamojiapp.app.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.naijamojiapp.app.roomDB.dao.UserDao;
import com.naijamojiapp.app.roomDB.entity.AllStickerList;
import com.naijamojiapp.app.roomDB.entity.CatListTab;
import com.naijamojiapp.app.roomDB.entity.RecentStickerList;
import com.naijamojiapp.app.roomDB.entity.TagList;

@Database(entities = {AllStickerList.class, CatListTab.class , TagList.class , RecentStickerList.class}, version = 5,exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mInstance;
    private static final String DATABASE_NAME = "NaijamojiDB";

    public abstract UserDao userDao();

    public synchronized static AppDatabase getDatabaseInstance(Context context) {
        if (mInstance == null) {
            mInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }

}