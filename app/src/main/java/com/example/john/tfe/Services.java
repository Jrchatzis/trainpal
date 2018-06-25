package com.example.john.tfe;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonDeserialize(builder = ServicesBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Services {

    @Nullable @JsonProperty("last_updated")
    Integer getLastUpdated();

    List<Service> getServices();

}
