package com.example.john.routing.api;

import com.example.john.config.TrainStationInfo;

public interface RoutingService {

    RoutingResult route(TrainStationInfo departureStation, TrainStationInfo destinationStation) throws Exception;

}
