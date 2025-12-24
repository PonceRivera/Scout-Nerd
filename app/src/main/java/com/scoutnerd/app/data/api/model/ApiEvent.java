package com.scoutnerd.app.data.api.model;

import com.google.gson.annotations.SerializedName;

public class ApiEvent {
    @SerializedName("key")
    public String key;

    @SerializedName("name")
    public String name;

    @SerializedName("event_code")
    public String eventCode;

    @SerializedName("event_type_string")
    public String eventTypeString;

    @SerializedName("start_date")
    public String startDate;

    @SerializedName("end_date")
    public String endDate;

    @SerializedName("city")
    public String city;
}
