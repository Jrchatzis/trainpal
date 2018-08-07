package com.example.john.config;

public enum TrainStationInfo {

    //Train stations key name accompanied by their full name and locations
    EDB("Waverley station", 55.951962f,-3.189944f),
    HYM("Haymarket station",55.945598f,-3.218281f),
    EDP("Edinburgh Park", 55.927663f,-3.307743f),
    SGL("South Gyle", 55.936392f,-3.298936f);

    private String station;
    private float lat;
    private float lon;

    //Constructor of Train stations
    TrainStationInfo(String station, float lat, float lon) {
        this.station = station;
        this.lat = lat;
        this.lon = lon;
    }
    //Method returning the train station name
    public String getStation() {

        return station;
    }
    //Method returning the latitude of the train station
    public float getLat() {

        return lat;
    }
    //Method returning the longitude of the train station
    public float getLon() {

        return lon;
    }
    //Method returning train station name as string
    @Override
    public String toString() {
        return station;
    }
}
