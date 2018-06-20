package com.example.john.tfe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stops {

    @JsonProperty("last_updated")
    private int lastUpdated;

    private List<Stop> stops;
}
