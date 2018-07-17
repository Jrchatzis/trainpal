package com.example.john.routing.api;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityRoute;
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.example.john.tfe.Service;

import org.immutables.value.Value;

import java.util.List;

//Setting the interface of the routing result
@Value.Immutable
public interface RoutingResult {

    GraphicsOverlay getGraphicsOverlay();
    Envelope getFullExtent();
    List<DirectionManeuver> getDirectionManeuvers();

}
