package com.example.john.routing.services;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityResult;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityRoute;
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.example.john.config.TaxiRankInfo;
import com.example.john.config.TrainStationInfo;
import com.example.john.routing.api.RoutingResult;
import com.example.john.routing.api.RoutingResultBuilder;
import com.example.john.routing.api.RoutingService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Routing by taxi
 */
public class TaxiRoutingService implements RoutingService {

    private final Resources res;
    private final Context ctx;
    private final EsriService esriService;


    public TaxiRoutingService(Activity activity, EsriService esriService) {
        this.res = activity.getApplicationContext().getResources();
        this.ctx = activity.getApplicationContext();
        this.esriService = esriService;
    }

    @Override
    public RoutingResult route(TrainStationInfo departureStation, TrainStationInfo destinationStation) throws Exception {

        ClosestFacilityResult closestTaxiRankResult = esriService.getClosestFacilityResult(
                // incidents
                Stream.of(departureStation)
                        .map(si -> new Point(
                                si.getLon(),
                                si.getLat(),
                                SpatialReferences.getWgs84()))
                        ::iterator,
                // facilities
                Stream.of(TaxiRankInfo.values())
                        .map(t -> new Point(
                                t.getLon(),
                                t.getLat(),
                                SpatialReferences.getWgs84()))
                        ::iterator);

        Integer closestTaxiRankIndex = closestTaxiRankResult.getRankedFacilityIndexes(0).get(0);
        ClosestFacilityRoute walkingRoute = closestTaxiRankResult.getRoute(closestTaxiRankIndex, 0);
        TaxiRankInfo closestTaxiRankInfo = TaxiRankInfo.values()[closestTaxiRankIndex];
        RouteResult taxiRoute = esriService.getRouteResult(
                walkingRoute.getEndTime(),
                "driving time",
                new Point(closestTaxiRankInfo.getLon(), closestTaxiRankInfo.getLat(), SpatialReferences.getWgs84()),
                new Point(destinationStation.getLon(), destinationStation.getLat(), SpatialReferences.getWgs84()));


        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        LineSymbol taxiRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 4.0f);
        LineSymbol pedestrianRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 2.0f);


        List<Polyline> taxiGeometries = taxiRoute.getRoutes().stream().map(Route::getRouteGeometry).collect(Collectors.toList());
        // get the taxi
        taxiGeometries.forEach(g -> {
            graphicsOverlay.getGraphics().add(new Graphic(g, taxiRouteSymbol));
        });

        // walk to the closest taxi rank
        Polyline firstRouteGeometry = walkingRoute.getRouteGeometry();
        graphicsOverlay.getGraphics().add(new Graphic(firstRouteGeometry, pedestrianRouteSymbol));

        Envelope fullExtent = GeometryEngine.union(Stream.concat(Stream.of(firstRouteGeometry), taxiGeometries.stream()).collect(toList())).getExtent();
        List<DirectionManeuver> directionManeuvers = Stream
                .concat(
                    // directions for walking to the closest taxi rank
                        walkingRoute.getDirectionManeuvers().stream(),
                    // directions for traveling with the taxi
                        taxiRoute.getRoutes().stream().map(Route::getDirectionManeuvers).flatMap(List::stream)
                )
                .collect(toList());

        return new RoutingResultBuilder()
                .graphicsOverlay(graphicsOverlay)
                .directionManeuvers(directionManeuvers)
                .fullExtent(fullExtent)
                .build();
    }
}
