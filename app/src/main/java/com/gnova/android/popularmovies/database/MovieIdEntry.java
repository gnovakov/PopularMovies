package com.gnova.android.popularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movieId")
public class MovieIdEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String movId;

    @Ignore
    public MovieIdEntry(String movId) {
        this.movId = movId;
    }

    public MovieIdEntry(int id, String movId) {
        this.id = id;
        this.movId = movId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovId() {
        return movId;
    }

    public void setMovId(String movId) {
        this.movId = movId;
    }


}
