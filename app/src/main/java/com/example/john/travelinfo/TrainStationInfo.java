package com.example.john.travelinfo;

public enum TrainStationInfo {

    EDB("Waverley","55.951962","-3.189944"),
    HYM("Haymarket","55.945598","-3.218281"),
    EDP("Edinburgh Park","55.927663","-3.307743"),
    EGY("Edinburgh Gateway", "55.940933","-3.320274");

    private String station;
    private String lat;
    private String lon;

    TrainStationInfo(String station,String lat, String lon) {
        this.station = station;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStation() {
        return station;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
