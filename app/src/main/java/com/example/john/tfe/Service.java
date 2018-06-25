package com.example.john.tfe;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.immutables.value.Value;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Value.Immutable
@JsonDeserialize(builder = ServiceBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Service {

    @Nullable
    String getName();

    @Nullable @Value.Auxiliary
    String getDescription();

    @Nullable @Value.Auxiliary
    @JsonProperty("service_type")
    String getServiceType();

    @Value.Auxiliary
    List<Route> getRoutes();

    @Value.Lazy
    default Stream<Integer> getStopIds() {
        return getRoutes().stream()
                .map(Route::getStops)
                .flatMap(List::stream);
    }

    @Value.Immutable
    @JsonDeserialize(builder = RouteBuilder.class)
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static interface Route {

        String getDestination();

        @Value.Auxiliary
        List<Point> getPoints();

        @Value.Auxiliary
        List<Integer> getStops();
    }

    @Value.Immutable
    @JsonDeserialize(builder = PointBuilder.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static interface Point {

        @JsonProperty("stop_id")
        int getStopId();

        @Value.Auxiliary
        float getLatitude();

        @Value.Auxiliary
        float getLongitude();

    }
}
