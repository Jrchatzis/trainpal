package com.example.john.routing.api;

import com.example.john.config.TrainStationInfo;

//Setting the interface for the routing result
public interface RoutingService {

    RoutingResult route(TrainStationInfo departureStation, TrainStationInfo destinationStation) throws Exception;

}
