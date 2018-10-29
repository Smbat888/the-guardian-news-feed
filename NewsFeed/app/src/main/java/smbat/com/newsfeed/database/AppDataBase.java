package smbat.com.newsfeed.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import smbat.com.newsfeed.database.dao.NewsDao;
import smbat.com.newsfeed.database.entities.News;

@Database(entities = {News.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static final String APP_DATABASE_NAME = "news-db";

    private static AppDataBase instance;


    public abstract NewsDao newsDao();

    public static AppDataBase getAppDatabase(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class, APP_DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
