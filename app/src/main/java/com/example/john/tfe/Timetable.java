package com.example.john.tfe;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonDeserialize(builder = TimetableBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Timetable {

    @JsonProperty("stop_id")
    int getStopId();

    @Nullable @Value.Auxiliary @JsonProperty("stop_name")
    String getStopName();

    @Value.Auxiliary
    List<Departure> getDepartures();

}
