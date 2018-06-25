package com.example.john.tfe;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(builder = DepartureBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Departure {

    @Nullable
    @JsonProperty("service_name")
    String getServiceName();

    @Nullable
    String getTime();

    @Nullable
    String getDestination();

    int getDay();

    @Nullable
    @JsonProperty("note_id")
    String getNoteId();

    @JsonProperty("valid_from")
    int getValidFrom();

}
