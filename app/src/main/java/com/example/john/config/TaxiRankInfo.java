package com.example.john.config;

//Location of taxi ranks
public enum TaxiRankInfo {
    
    TaxiRank1(55.952669,-3.174778),
    TaxiRank2(55.951975,-3.203194),
    TaxiRank3(55.947159,-3.190923),
    TaxiRank4(55.926844,-3.308646),
    TaxiRank5(55.944902,-3.204777),
    TaxiRank6(55.951695,-3.199535),
    TaxiRank7(55.948239,-3.191941),
    TaxiRank8(55.95388,-3.194773),
    TaxiRank9(55.953186,-3.198813),
    TaxiRank10(55.952657,-3.201936),
    TaxiRank11(55.953224,-3.199448),
    TaxiRank12(55.947149,-3.196818),
    TaxiRank13(55.947838,-3.194379),
    TaxiRank14(55.946604,-3.216892),
    TaxiRank15(55.952532,-3.196758),
    TaxiRank16(55.950229,-3.187023),
    TaxiRank17(55.950974,-3.190791),
    TaxiRank18(55.949164,-3.194165),
    TaxiRank19(55.947457,-3.206109),
    TaxiRank20(55.945834,-3.209886),
    TaxiRank21(55.946114,-3.185661),
    TaxiRank22(55.950335,-3.187547),
    TaxiRank23(55.955339,-3.192609),
    TaxiRank24(55.951009,-3.210012),
    TaxiRank25(55.949891,-3.207562),
    TaxiRank26 (55.953668,-3.192765),
    TaxiRank27(55.953687,-3.187137),
    TaxiRank28(55.952176,-3.19191);


    private double lat;
    private double lon;

    //Constructor of coordinates
    TaxiRankInfo (double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
//Method getting the latitude of the taxi ranks
    public double getLat() {
        return lat;
    }

//Method getting the longitude of the taxi ranks
    public double getLon() {
        return lon;
    }
}
