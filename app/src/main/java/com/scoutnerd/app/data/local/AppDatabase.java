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

import com.scoutnerd.app.data.local.dao.MatchResultDao;
import com.scoutnerd.app.data.local.dao.MetricDao;
import com.scoutnerd.app.data.local.entity.MatchResultEntity;
import com.scoutnerd.app.data.local.entity.MetricEntity;

@Database(entities = { EventEntity.class, TeamEntity.class, EventTeamJoin.class, MetricEntity.class,
        MatchResultEntity.class }, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EventDao eventDao();

    public abstract TeamDao teamDao();

    public abstract MetricDao metricDao();

    public abstract MatchResultDao matchResultDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "scout_nerd_database")
                            .fallbackToDestructiveMigration() // Simple migration for dev
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
