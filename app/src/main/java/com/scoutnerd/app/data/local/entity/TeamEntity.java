package com.scoutnerd.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "teams")
public class TeamEntity {
    @PrimaryKey
    @NonNull
    public String key; // e.g., "frc118"

    public int teamNumber;
    public String nickname;
    public String name;
    public String city;

    // Foreign key to event isn't strictly necessary if we query by relationship,
    // but for caching lists per event, we might want a join table or just store
    // "eventKey"
    // if teams are unique to events? No, teams go to multiple events.
    // For simplicity, we will just store teams globally.
    // Handling "Teams at Event" might require a Join Table or just fetching from
    // API dynamically.
    // Let's store a basic list for now.

    public TeamEntity(@NonNull String key, int teamNumber, String nickname, String name, String city) {
        this.key = key;
        this.teamNumber = teamNumber;
        this.nickname = nickname;
        this.name = name;
        this.city = city;
    }
}
