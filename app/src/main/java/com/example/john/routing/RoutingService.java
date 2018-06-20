package com.example.john.routing;

import com.example.john.travelinfo.TrainStationInfo;

public interface RoutingService {

    RoutingResult route(TrainStationInfo departureStation, TrainStationInfo destinationStation) throws Exception;

}
