package com.example.john.tfe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {

    private String name;
    private String description;

    @JsonProperty("service_type")
    private String serviceType;

    private List<Route> routes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        private String destination;
        private List<Point> points;
        private List<Integer> stops;

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public List<Point> getPoints() {
            return points;
        }

        public void setPoints(List<Point> points) {
            this.points = points;
        }

        public List<Integer> getStops() {
            return stops;
        }

        public void setStops(List<Integer> stops) {
            this.stops = stops;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Point {
        @JsonProperty("stop_id")
        private String stopId;
        private float latitude;
        private float longitude;

        public String getStopId() {
            return stopId;
        }

        public void setStopId(String stopId) {
            this.stopId = stopId;
        }

        public float getLatitude() {
            return latitude;
        }

        public void setLatitude(float latitude) {
            this.latitude = latitude;
        }

        public float getLongitude() {
            return longitude;
        }

        public void setLongitude(float longitude) {
            this.longitude = longitude;
        }
    }
}
