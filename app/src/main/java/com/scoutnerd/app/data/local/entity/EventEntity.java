package com.scoutnerd.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class EventEntity {
    @PrimaryKey
    @NonNull
    public String key;

    public String name;
    public String eventCode;
    public String startDate;
    public String city;

    public EventEntity(@NonNull String key, String name, String eventCode, String startDate, String city) {
        this.key = key;
        this.name = name;
        this.eventCode = eventCode;
        this.startDate = startDate;
        this.city = city;
    }
}
