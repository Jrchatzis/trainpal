package com.example.john.tfe;

import retrofit2.http.GET;
import retrofit2.http.Path;

//Interface of extracting stops, services and timetables from tfe
public interface TfeOpenDataService {

    @GET("/stops")
    Stops getStops();

    @GET("/services")
    Services getServices();

    @GET("/timetables/{stop_id}")
    Timetable getTimetable(@Path("stop_id") int stopId);

}
