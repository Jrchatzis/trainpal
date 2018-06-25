package com.example.john.tfe;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.immutables.value.Value;

import java.util.List;
import java.util.Objects;

@Value.Immutable
@JsonDeserialize(builder = StopBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Stop {

    @JsonProperty("stop_id")
    Integer getStopId();

    @Nullable @Value.Auxiliary @JsonProperty("atco_code")
    String getAtcoCode();

    @Nullable @Value.Auxiliary
    String getName();

    @Nullable @Value.Auxiliary
    String getIdentifier();

    @Nullable @Value.Auxiliary
    String getLocality();

    @Nullable @Value.Auxiliary
    Integer getOrientation();

    @Nullable @Value.Auxiliary
    String getDirection();

    @Value.Auxiliary
    float getLatitude();

    @Value.Auxiliary
    float getLongitude();

    @Nullable @Value.Auxiliary @JsonProperty("service_type")
    String getServiceType();

    @Value.Auxiliary
    List<String> getDestinations();

    @Value.Auxiliary
    List<String> getServices();

    @Nullable @Value.Auxiliary @JsonProperty("atco_latitude")
    Float getAtcoLatitude();

    @Nullable @Value.Auxiliary @JsonProperty("atco_longitude")
    Float getAtcoLongitude();

}
