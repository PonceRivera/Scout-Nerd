package com.scoutnerd.app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "event_teams", primaryKeys = { "eventKey", "teamKey" }, foreignKeys = {
        @ForeignKey(entity = EventEntity.class, parentColumns = "key", childColumns = "eventKey", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = TeamEntity.class, parentColumns = "key", childColumns = "teamKey", onDelete = ForeignKey.CASCADE)
})
public class EventTeamJoin {
    @NonNull
    public String eventKey;
    @NonNull
    public String teamKey;

    public EventTeamJoin(@NonNull String eventKey, @NonNull String teamKey) {
        this.eventKey = eventKey;
        this.teamKey = teamKey;
    }
}
