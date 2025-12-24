package com.scoutnerd.app.data.api.model;

import com.google.gson.annotations.SerializedName;

public class ApiTeam {
    @SerializedName("key")
    public String key;

    @SerializedName("team_number")
    public int teamNumber;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("name")
    public String name;

    @SerializedName("city")
    public String city;
}
