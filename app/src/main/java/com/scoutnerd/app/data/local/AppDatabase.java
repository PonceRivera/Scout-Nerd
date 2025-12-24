package com.scoutnerd.app.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.scoutnerd.app.data.local.dao.EventDao;
import com.scoutnerd.app.data.local.dao.TeamDao;
import com.scoutnerd.app.data.local.entity.EventEntity;
import com.scoutnerd.app.data.local.entity.EventTeamJoin;
import com.scoutnerd.app.data.local.entity.TeamEntity;

@Database(entities = {EventEntity.class, TeamEntity.class, EventTeamJoin.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
    public abstract TeamDao teamDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "scout_nerd_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
