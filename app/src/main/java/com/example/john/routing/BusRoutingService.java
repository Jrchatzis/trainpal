package com.example.john.routing;

import android.content.Context;
import android.content.res.Resources;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityResult;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityRoute;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.esri.arcgisruntime.tasks.networkanalysis.Incident;
import com.example.john.tfe.Service;
import com.example.john.tfe.TfeOpenDataService;
import com.example.john.tfe.TfeOpenDataServiceFactory;
import com.example.john.travelinfo.R;
import com.example.john.travelinfo.TrainStationInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusRoutingService implements RoutingService {

    private static String URL = "http://sampleserver6.arcgisonline.com/arcgis/rest/services/NetworkAnalysis/SanDiego/NAServer/ClosestFacility";
    private final Resources res;
    private final Context ctx;
    Map<String,List<String>> busServicesMap;

    public BusRoutingService(Resources res, Context ctx) {
        this.res = res;
        this.ctx = ctx;
        try (InputStream is = res.openRawResource(R.raw.bus_services)) {
            busServicesMap = new ObjectMapper().readValue(is, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RoutingResult route(TrainStationInfo departureStation, TrainStationInfo destinationStation) throws Exception {

        // TODO: find a valid access token or implement the service by hand.
        TfeOpenDataService busService = TfeOpenDataServiceFactory.getService("");
        String key = Stream.of(departureStation.name(), destinationStation.name()).sorted().collect(Collectors.joining("_"));
        List<String> busServicesWhitelist = busServicesMap.get(key);
        List<Integer> stopsWhitelist = busService.getServices().getServices().stream()
                .filter(service -> busServicesWhitelist.contains(service.getName()))
                .map(Service::getRoutes)
                .flatMap(List::stream)
                .map(Service.Route::getStops)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        final ClosestFacilityTask closestFacilityTask = new ClosestFacilityTask(ctx, URL);
        closestFacilityTask.loadAsync();
        ClosestFacilityParameters closestFacilityParameters = closestFacilityTask.createDefaultParametersAsync().get();
        // explicitly set values for some parameters
        closestFacilityParameters.setReturnDirections(true);
        closestFacilityParameters.setReturnRoutes(true);

        List<Incident> incidents = Arrays.asList(new Incident(new Point(departureStation.getLon(), departureStation.getLat(), SpatialReferences.getWebMercator())));
        closestFacilityParameters.setIncidents(incidents);

        List<Facility> facilities = busService.getStops().getStops().stream()
                .filter(stop -> stopsWhitelist.contains(stop.getStopId()))
                .map(stop -> new Facility(new Point(stop.getLongitude(), stop.getLatitude(), SpatialReferences.getWebMercator())))
                .collect(Collectors.toList());
        closestFacilityParameters.setFacilities(facilities);

        ClosestFacilityResult solvedResult = closestFacilityTask.solveClosestFacilityAsync(closestFacilityParameters).get();
        Integer facilityIndex = solvedResult.getRankedFacilityIndexes(0).get(0);
        Facility closestFacility = solvedResult.getFacilities().get(facilityIndex);
        ClosestFacilityRoute route = solvedResult.getRoute(facilityIndex, 0);
        RoutingResult result = new RoutingResult();
        result.setRoute(route);
        result.setFacility(closestFacility);
        return result;
    }


}
