package com.example.john.routing.services;

import android.content.Context;
import android.content.res.Resources;

import com.example.john.routing.api.RoutingResult;
import com.example.john.routing.api.RoutingService;
import com.example.john.config.TrainStationInfo;

/**
 * Routing by taxi
 */
public class TaxiRoutingService implements RoutingService {

    private final Resources res;
    private final Context ctx;
    private final EsriService esriService;

    public TaxiRoutingService(Resources res, Context ctx) {
        this.res = res;
        this.ctx = ctx;
        this.esriService = new EsriService(ctx);
    }

    @Override
    public RoutingResult route(TrainStationInfo departureStation, TrainStationInfo destinationStation) throws Exception {
        return null;
    }
}
