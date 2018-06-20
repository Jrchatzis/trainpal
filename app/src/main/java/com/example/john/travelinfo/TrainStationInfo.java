package com.example.john.travelinfo;

public enum TrainStationInfo {

    EDB("Waverley station", 55.951962f,-3.189944f),
    HYM("Haymarket station",55.945598f,-3.218281f),
    EDP("Edinburgh Park", 55.927663f,-3.307743f),
    EGY("Edinburgh Gateway", 55.940933f,-3.320274f);

    private String station;
    private float lat;
    private float lon;

    TrainStationInfo(String station, float lat, float lon) {
        this.station = station;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStation() {

        return station;
    }

    public float getLat() {

        return lat;
    }

    public float getLon() {

        return lon;
    }

    @Override
    public String toString() {
        return station;
    }
}
