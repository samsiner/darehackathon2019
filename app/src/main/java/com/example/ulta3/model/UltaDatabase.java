package com.example.ulta3.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Room database (station_table) for stations
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

@Database(entities = {Product.class}, version = 1, exportSchema = false)
public abstract class UltaDatabase extends RoomDatabase {
    public abstract RoomDao roomDao();

    private static volatile UltaDatabase INSTANCE;

    static UltaDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UltaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UltaDatabase.class, "ulta_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}