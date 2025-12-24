package com.scoutnerd.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.scoutnerd.app.data.local.entity.MetricEntity;
import java.util.List;

@Dao
public interface MetricDao {
    @Insert
    void insertAll(List<MetricEntity> metrics);

    @Insert
    void insert(MetricEntity metric);

    @Query("SELECT * FROM metrics ORDER BY sortOrder ASC")
    LiveData<List<MetricEntity>> getAllMetrics();

    @Query("SELECT * FROM metrics ORDER BY sortOrder ASC")
    List<MetricEntity> getAllMetricsSync();

    @Query("SELECT COUNT(*) FROM metrics")
    int getCount();
}
