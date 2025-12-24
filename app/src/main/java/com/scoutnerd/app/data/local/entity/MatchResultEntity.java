package com.scoutnerd.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "match_results", foreignKeys = @ForeignKey(entity = MetricEntity.class, parentColumns = "id", childColumns = "metricId", onDelete = ForeignKey.CASCADE), indices = {
        @Index("metricId") })
public class MatchResultEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public int matchNumber;
    public int teamNumber;
    public String eventKey;
    public long metricId;
    public String value; // Stored as String for flexibility
    public long timestamp;

    public MatchResultEntity(int matchNumber, int teamNumber, String eventKey, long metricId, String value,
            long timestamp) {
        this.matchNumber = matchNumber;
        this.teamNumber = teamNumber;
        this.eventKey = eventKey;
        this.metricId = metricId;
        this.value = value;
        this.timestamp = timestamp;
    }
}
