package com.example.john.routing.api;

import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityRoute;
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface RoutingResult {

    GraphicsOverlay getGraphicsOverlay();

    List<DirectionManeuver> getDirectionManeuvers();

}
