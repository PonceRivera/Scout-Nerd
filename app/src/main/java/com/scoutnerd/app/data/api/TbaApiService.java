package com.scoutnerd.app.data.api;

import com.scoutnerd.app.data.api.model.ApiEvent;
import com.scoutnerd.app.data.api.model.ApiTeam;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TbaApiService {
    @GET("events/{year}/simple")
    Call<List<ApiEvent>> getEvents(@Path("year") int year);

    @GET("event/{eventKey}/teams")
    Call<List<ApiTeam>> getTeamsAtEvent(@Path("eventKey") String eventKey);
}
