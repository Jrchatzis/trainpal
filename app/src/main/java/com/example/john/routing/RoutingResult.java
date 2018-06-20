package com.example.john.routing;

import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityRoute;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;

public class RoutingResult {

    Facility facility;
    ClosestFacilityRoute route;

    public ClosestFacilityRoute getRoute() {
        return route;
    }

    public void setRoute(ClosestFacilityRoute route) {
        this.route = route;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
