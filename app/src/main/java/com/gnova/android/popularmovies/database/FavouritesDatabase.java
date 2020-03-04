package com.gnova.android.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieIdEntry.class}, version = 1, exportSchema = false)
public abstract class FavouritesDatabase extends RoomDatabase {

    private static final String LOG_TAG = FavouritesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favourites";
    private static FavouritesDatabase sInstance;

    public static FavouritesDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        FavouritesDatabase.class, FavouritesDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;


    }

    public abstract MovieIdDao movieIdDao();

}
