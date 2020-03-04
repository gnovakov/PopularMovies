package com.gnova.android.popularmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieIdDao {

    @Query("SELECT * FROM movieId ORDER BY movId")
    List<MovieIdEntry> loadAllTasks();

    @Insert
    void insertMovid(MovieIdEntry movieIdEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovieId(MovieIdEntry movieIdEntry);

    @Delete
    void deleteMovieId(MovieIdEntry movieIdEntry);


}
