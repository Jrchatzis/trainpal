package com.example.john.tfe;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(builder = DepartureBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)

//Setting the interface for the departing buses and trams
public interface Departure {

    @Nullable
    @JsonProperty("service_name")
    String getServiceName();

    @Nullable
    String getTime();

    @Nullable
    String getDestination();

    @Nullable
    Integer getDay();

    @Nullable
    @JsonProperty("note_id")
    String getNoteId();

    @Nullable @JsonProperty("valid_from")
    Integer getValidFrom();

}
