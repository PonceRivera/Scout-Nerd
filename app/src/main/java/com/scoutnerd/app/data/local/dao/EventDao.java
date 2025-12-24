package com.scoutnerd.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.scoutnerd.app.data.local.entity.EventEntity;
import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM events ORDER BY startDate DESC")
    LiveData<List<EventEntity>> getAllEvents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvents(List<EventEntity> events);
}
