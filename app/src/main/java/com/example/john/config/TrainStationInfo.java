package com.example.john.config;

public enum TrainStationInfo {

    //Train stations key name accompanied by their full name and locations
    EDB("Waverley station", 55.951962f,-3.189944f),
    HYM("Haymarket station",55.945598f,-3.218281f),
    EDP("Edinburgh Park", 55.927663f,-3.307743f),
    EGY("Edinburgh Gateway", 55.940933f,-3.320274f);

    private String station;
    private float lat;
    private float lon;

    //Method referring to the train stations informations
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

    @Override
    public String toString() {
        return station;
    }
}
