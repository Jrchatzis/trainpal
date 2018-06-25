package com.example.john.tfe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonDeserialize(builder = StopsBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Stops {

    @JsonProperty("last_updated")
    int getLastUpdated();

    List<Stop> getStops();

}
