package smbat.com.newsfeed.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import smbat.com.newsfeed.database.entities.News;

@Dao
public interface NewsDao {

    @Insert
    void insert(News... person);

    @Update
    void update(News... person);

    @Delete
    void delete(News... person);

    @Query("Select * FROM news")
    News[] loadAll();
}