package com.example.john.tfe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stop {

    @JsonProperty("stop_id")
    private int stopId;

    @JsonProperty("atco_code")
    private String atcoCode;

    private String name;

    private String identifier;

    private String locality;

    private int orientation;

    private String direction;

    private float latitude;

    private float longitude;

    @JsonProperty("service_type")
    private String serviceType;

    private List<String> destinations;

    private List<String> services;

    @JsonProperty("atco_latitude")
    private float atcoLatitude;

    @JsonProperty("atco_longitude")
    private float atcoLongitude;

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public String getAtcoCode() {
        return atcoCode;
    }

    public void setAtcoCode(String atcoCode) {
        this.atcoCode = atcoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public float getAtcoLatitude() {
        return atcoLatitude;
    }

    public void setAtcoLatitude(float atcoLatitude) {
        this.atcoLatitude = atcoLatitude;
    }

    public float getAtcoLongitude() {
        return atcoLongitude;
    }

    public void setAtcoLongitude(float atcoLongitude) {
        this.atcoLongitude = atcoLongitude;
    }
}
