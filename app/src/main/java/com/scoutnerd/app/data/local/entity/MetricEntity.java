package com.scoutnerd.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "metrics")
public class MetricEntity {

    public static final int TYPE_BOOLEAN = 0;
    public static final int TYPE_COUNTER = 1;
    public static final int TYPE_SLIDER = 2;
    public static final int TYPE_TEXT = 3;

    public static final int CATEGORY_AUTO = 0;
    public static final int CATEGORY_TELEOP = 1;
    public static final int CATEGORY_ENDGAME = 2;
    public static final int CATEGORY_PIT = 3;

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public int type;
    public int category;
    public int sortOrder;

    public MetricEntity(String name, int type, int category, int sortOrder) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.sortOrder = sortOrder;
    }
}
