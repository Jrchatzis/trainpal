package com.example.john.routing;

import android.content.Context;
import android.content.res.Resources;

import com.example.john.travelinfo.TrainStationInfo;

public class TaxiRoutingService implements RoutingService {

    private static String URL = "http://sampleserver6.arcgisonline.com/arcgis/rest/services/NetworkAnalysis/SanDiego/NAServer/ClosestFacility";
    private final Resources res;
    private final Context ctx;

    public TaxiRoutingService(Resources res, Context ctx) {
        this.res = res;
        this.ctx = ctx;

    }
    @Override
    public RoutingResult route(TrainStationInfo departureStation, TrainStationInfo destinationStation) throws Exception {
        return null;
    }
}
