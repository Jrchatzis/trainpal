package com.example.john.routing.services;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.codepoetics.protonpack.StreamUtils;
import com.esri.arcgisruntime.geometry.CoordinateFormatter;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityResult;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityRoute;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityTask;
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.esri.arcgisruntime.tasks.networkanalysis.Incident;
import com.example.john.routing.api.Direction;
import com.example.john.routing.api.DirectionBuilder;
import com.example.john.routing.api.RoutingResult;
import com.example.john.routing.api.RoutingResultBuilder;
import com.example.john.routing.api.RoutingService;
import com.example.john.tfe.Departure;
import com.example.john.tfe.Service;
import com.example.john.tfe.Stop;
import com.example.john.tfe.TfeOpenDataService;
import com.example.john.tfe.TfeOpenDataServiceFactory;
import com.example.john.tfe.Timetable;
import com.example.john.travelinfo.R;
import com.example.john.config.TrainStationInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.immutables.value.Value;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Routing by bus
 */
public class BusRoutingService implements RoutingService {

    private final Resources res;
    private final Context ctx;
    private final EsriService esriService;
    private Map<String,List<String>> servicesByStationPair;
    private Map<Integer, Stop> allStops;
    private Map<String, Service> allServices;
    private TfeOpenDataService busService;

    ///**
     //* Constructor
     //* @param ctx
     //*/
    public BusRoutingService(Activity activity, EsriService esriService) {
        this.res = activity.getApplicationContext().getResources();
        this.ctx = activity.getApplicationContext();
        this.esriService = esriService;
        this.busService = TfeOpenDataServiceFactory.getLocalService(ctx);
        try (InputStream is = res.openRawResource(R.raw.bus_services)) {
            this.servicesByStationPair = (Map<String,List<String>>)new ObjectMapper().readValue(is, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.allServices = busService.getServices().getServices()
                .stream()
                .collect(toMap(Service::getName, java.util.function.Function.identity()));

        this.allStops = busService.getStops().getStops()
                .stream()
                .collect(toMap(Stop::getStopId, java.util.function.Function.identity()));
    }

    @Override
    public RoutingResult route(TrainStationInfo departureStation, TrainStationInfo destinationStation) throws Exception {

        // calculate key to find candidate service names from bus_services.json
        String stationPairKey = Stream
                .of(
                    departureStation.name(),
                    destinationStation.name())
                .sorted()
                .collect(joining("_"));
        List<String> allowedServiceNames = servicesByStationPair.get(stationPairKey);
        Log.i(BusRoutingService.class.getSimpleName(), "allowedServiceNames: " + allowedServiceNames);

        //Collect the names of the allowed services
        Predicate<Service> allowedServiceFilter = service -> allowedServiceNames.contains(service.getName());
        //Collect the allowed services based on the train station combination key
        Map<String,Service> allowedServices = allServices.values().stream().filter(allowedServiceFilter).collect(toMap(Service::getName, java.util.function.Function.identity()));
        //Collect the ids of the allowed stops based on the allowed services
        List<Integer> allowedStopIds = allowedServices.values().stream().flatMap(Service::getStopIds).distinct().collect(toList());
        Log.i(BusRoutingService.class.getSimpleName(), String.format("allowedStopIds (%s): %s", allowedStopIds.size(), allowedStopIds));
        //Collect the allowed stops based on their ids
        List<Stop> allowedStops = allStops.values().stream()
                .filter(stop -> allowedStopIds.contains(stop.getStopId()))
                .collect(toList());
        Log.i(BusRoutingService.class.getSimpleName(), String.format("allowedStops (%s): %s", allowedStops.size(), allowedStops));

        //Connect stop ids with their values
        Map<Integer,Integer> stopIdToStopIndex = new HashMap<>();
        for (int i=0; i<allowedStops.size(); i++) {
            stopIdToStopIndex.put(allowedStops.get(i).getStopId(), i);
        }

        //Calculate the closest facility
        ClosestFacilityResult closestFacilityResult = esriService.getClosestFacilityResult(
                // Create a list of incidents accompanied by their lat and lon
                Stream.of(departureStation, destinationStation)
                        .map(si -> new Point(
                                si.getLon(),
                                si.getLat(),
                                SpatialReferences.getWgs84()))
                        ::iterator,
                // Create a list of facilities accompanied by their lat and lon
                allowedStops.stream()
                        .map(s -> new Point(
                                s.getLongitude(),
                                s.getLatitude(),
                                SpatialReferences.getWgs84()))
                        ::iterator);

        //Create new array list of departures
        List<DepartureFacility> departures = new ArrayList<>();

        //Create list of ranked destination stops
        List<Stop> rankedDestinationStops = closestFacilityResult.getRankedFacilityIndexes(1).stream().map(allowedStops::get).collect(Collectors.toList());
        Log.i(BusRoutingService.class.getSimpleName(), "rankedDestinationStops: " + rankedDestinationStops);

        //Create list with the remaining stops of the service
        for (int i=0; i<allowedStops.size(); i++) {
            Stop stop = allowedStops.get(i);
            List<DepartingService> departingServices = stop.getServices()
                    .stream()
                    .filter(allowedServiceNames::contains)
                    .map(allowedServices::get)
                    .map(service -> toDepartingService(service, stop, rankedDestinationStops))
                    .filter(Objects::nonNull)
                    .collect(toList());

            if (!departingServices.isEmpty()) {
                DepartureFacility f = new DepartureFacilityBuilder()
                        .index(i)
                        .stop(stop)
                        .departingServices(departingServices)
                        .build();
                departures.add(f);
            }
        }

        // Hydrate departures list with departure and arrival times using ESRI's service and timetables
        Log.i(BusRoutingService.class.getSimpleName(), String.format("Trying to hydrate %s departure facilities", departures.size()));
        List<DepartureFacility> hydratedDepartures = departures
                .stream()
                .map(departureFacility -> {
                    String stopIdentity = String.format("[id: %s, name: %s]", departureFacility.getStop().getStopId(), departureFacility.getStop().getName());
                    ClosestFacilityRoute route  = closestFacilityResult.getRoute(departureFacility.getIndex(), 0);
                    if (route == null) {
                        //Log.w(BusRoutingService.class.getSimpleName(), String.format("Ignoring departure facility for stop %s since there is no ClosestFacilityRoute for that stop", stopIdentity));
                        return null;
                    }
                    LocalDateTime arrivalTimeToFacility = toLocalDateTime(route.getEndTime());
                    LocalTime nbf = arrivalTimeToFacility.toLocalTime(); // not before

                    Timetable departureTimetable = busService.getTimetable(departureFacility.getStop().getStopId());
                    if (departureTimetable == null) {
                        Log.w(BusRoutingService.class.getSimpleName(), String.format("Ignoring departure facility for stop %s since there is no timetable for that stop", stopIdentity));
                        return null;
                    }
                    List<DepartingService> hydratedDepartingServices = departureFacility.getDepartingServices()
                            .stream()
                            .map(departingService -> {
                                String departingServiceIdentity = String.format("[name: %s, destination: %s]", departingService.getName(), departingService.getDestination());
                                // Calculate departure time
                                LocalDateTime departureTime = nextArrival(departingService, departureTimetable, nbf);
                                if (departureTime == null) {
                                    return null;
                                }
                                // Get stop ID of service's destination
                                Timetable destinationTimetable = busService.getTimetable(departingService.getLastPoint().getStopId());
                                if (destinationTimetable == null) {
                                    Log.w(BusRoutingService.class.getSimpleName(), String.format("Ignoring departure facility for stop %s since there is no timetable for the destination stop %s", stopIdentity, departingService.getLastPoint().getStopId()));
                                    return null;
                                }
                                // Calculate arrival time
                                LocalDateTime arrivalTime = nextArrival(departingService, destinationTimetable, nbf);
                                if (arrivalTime == null) {
                                    return null;
                                }
                                //Second route to be followed by the user after dropping off at the destined bus station
                                ClosestFacilityRoute route2  = closestFacilityResult.getRoute(stopIdToStopIndex.get(departingService.getLastPoint().getStopId()), 1);
                                if (route2 == null) {
                                    return null;
                                }

                                //Builder of the total route followed by the user
                                return new DepartingServiceBuilder()
                                        .from(departingService)
                                        .firstRoute(route)
                                        .departureTime(departureTime)
                                        .arrivalTime(arrivalTime)
                                        .secondRoute(route2)
                                        .build();

                            })
                            .filter(Objects::nonNull)
                            .collect(toList());

                    if (hydratedDepartingServices.size() == 0) {
                        Log.w(BusRoutingService.class.getSimpleName(),
                                String.format("Ignoring departure facility for stop %s since the hydrated departing services list is empty (timetable issue?)", stopIdentity));
                        return null;
                    }
                    //Builder of the departure facilities
                    return new DepartureFacilityBuilder()
                            .from(departureFacility)
                            .arrivalTime(arrivalTimeToFacility)
                            .departingServices(hydratedDepartingServices)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Log.i(BusRoutingService.class.getSimpleName(), String.format("Hydrated %s departure facilities", hydratedDepartures.size()));

        if (hydratedDepartures.size() == 0) {
            Log.e(BusRoutingService.class.getSimpleName(),
                    String.format("No bus route could be calculated due to missing/corrupted data..."));
            return null;
        }

        // Now we should be able to rank the departure facilities...
        DepartureFacility fastestDeparture = hydratedDepartures.stream()
                .sorted(Comparator.comparing(d -> d.getFastestDepartingService().getTravelEndTime()))
                .findFirst()
                .get();

        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

        LineSymbol busRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 5.0f);
        LineSymbol pedestrianRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 2.0f);

        // walk to the closest bus station
        Polyline firstRouteGeometry = fastestDeparture.getFastestDepartingService().getFirstRoute().getRouteGeometry();
        graphicsOverlay.getGraphics().add(new Graphic(firstRouteGeometry, pedestrianRouteSymbol));

        // travel with the bus
        Polyline busStopGeometry = new Polyline(
                                        new PointCollection(
                                                Stream.concat(
                                                        Stream.of(fastestDeparture.getStop()),
                                                        fastestDeparture.getFastestDepartingService()
                                                            .getPoints()
                                                            .stream()
                                                    )
                                                    .map(stop -> new Point(stop.getLongitude(), stop.getLatitude(), SpatialReferences.getWgs84()))
                                                    .collect(toList())
                                        ));
        graphicsOverlay.getGraphics().add(new Graphic(busStopGeometry, busRouteSymbol));

        // walk from the bus station to the destination train station
        Geometry secondRouteGeometry = fastestDeparture.getFastestDepartingService().getSecondRoute().getRouteGeometry();
        graphicsOverlay.getGraphics().add(new Graphic(secondRouteGeometry, pedestrianRouteSymbol));
        Envelope fullExtent = GeometryEngine.union(Stream.of(firstRouteGeometry, busStopGeometry, secondRouteGeometry).collect(toList())).getExtent();

        //Produce the instructions to direct users about the services they should select and the time of the arrival to the terminal station
        DepartingService fastestDepartingService = fastestDeparture.getFastestDepartingService();
        String departingStop = fastestDeparture.getStop().getName();
        String serviceName = fastestDepartingService.getName();
        LocalDateTime serviceDeparture = fastestDepartingService.getDepartureTime();
        String dropoffStop = fastestDepartingService.getLastPoint().getName();
        LocalDateTime arrivalTime = fastestDepartingService.getTravelEndTime();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        Stream<Direction> busDirections = Stream.of(
                new DirectionBuilder().mode("bus").description(String.format(" Take service %s arriving at %s at bus stop %s", serviceName, timeFormatter.format(serviceDeparture) ,departingStop)).build(),
                new DirectionBuilder().mode("bus").description(String.format(" Drop off at bus stop %s. \n You will reach your destination at %s", dropoffStop, timeFormatter.format(arrivalTime))).build()
        );
        List<Direction> directionManeuvers = Stream
                .concat(
                        Stream.concat(
                                // directions for walking to the closest bus station
                                fastestDeparture.getFastestDepartingService().getFirstRoute().getDirectionManeuvers().stream().map(dm -> new DirectionBuilder().mode("walk").description(dm.getDirectionText()).build()),
                                // bus directions
                                busDirections),
                                // directions for walking from the bus station to the destination train station
                                fastestDeparture.getFastestDepartingService().getSecondRoute().getDirectionManeuvers().stream().map(dm -> new DirectionBuilder().mode("walk").description(dm.getDirectionText()).build())
                )
                .collect(toList());

        return new RoutingResultBuilder()
                .graphicsOverlay(graphicsOverlay)
                .directions(directionManeuvers)
                .fullExtent(fullExtent)
                .build();

    }

    /**
     * Calculates the next arrival of a service to a stop according to the stop's timetable
     * @param departingService the service we are interested in
     * @param timetable the stop's timetable
     * @param nbf the minimum time that the service is allowed to arrive
     * @return the service's next arrival
     */
    private LocalDateTime nextArrival(DepartingService departingService, Timetable timetable, LocalTime nbf) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
        Departure departure = timetable.getDepartures().stream()
                .filter(d -> d.getServiceName().equals(departingService.getName()))
                .filter(d -> d.getDestination().equals(departingService.getDestination()))
                .filter(d -> LocalTime.parse(d.getTime(), dtf).isAfter(nbf))
                .findFirst()
                .orElse(null);
        if (departure == null) {
            Log.e(BusRoutingService.class.getSimpleName(), String.format("no timetable entry found for service %s in stop %s", departingService.getName() + "(dest: "+departingService.getDestination()+")",  timetable.getStopId()));
            return null;
        }
        LocalTime arrivalTime = LocalTime.parse(departure.getTime(), dtf);
        LocalDateTime arrivalDateTime = LocalDateTime.now().withHour(arrivalTime.getHour()).withMinute(arrivalTime.getMinute());
        return arrivalDateTime;
    }

    /**
     * Maps a {@link Service} to a {@link DepartingService}
     * @param service the service to convert
     * @param departure the departing stop
     * @param allowedDestinations the list of allowed destinations
     * @return the departing service or <code>null</code> if no route was found in the service to satisfy the departing and destination stops
     */
    @Nullable
    private DepartingService toDepartingService(Service service, Stop departure, List<Stop> allowedDestinations) {
        if (service == null) {
            return null;
        }
        for (Service.Route route : service.getRoutes()) {
            // start from the next point after the departure
            List<Stop> stops = StreamUtils.skipUntilInclusive(
                            route.getPoints().stream(),
                            point -> point.getStopId() == departure.getStopId().intValue()
                    )
                    .map(point -> allStops.get(point.getStopId()))
                    .filter(Objects::nonNull)
                    .collect(toList());

            for (Stop allowedDestination : allowedDestinations) {
                if (stops.indexOf(allowedDestination) != -1) {
                    stops = stops.subList(0, stops.indexOf(allowedDestination)+1);
                    break;
                }
            }

            // the last stop should be contained in our destinations list, otherwise this route cannot be selected
            if (!stops.isEmpty() && allowedDestinations.contains(stops.get(stops.size()-1))) {
                DepartingService result = new DepartingServiceBuilder()
                        .name(service.getName())
                        .destination(route.getDestination())
                        .points(stops)
                        .build();
                return result;
            }
        }
        return null;
    }

    /**
     * Converts a {@link Calendar} to {@link LocalDateTime}
     * @param calendar the calendar
     * @return the result of the conversion
     */
    private static LocalDateTime toLocalDateTime(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        TimeZone tz = calendar.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        return LocalDateTime.ofInstant(calendar.toInstant(), zid);
    }

    @Value.Immutable
    @JsonDeserialize(builder = DepartureFacilityBuilder.class)
    public static interface DepartureFacility {

        int getIndex();

        Stop getStop();

        @Nullable
        LocalDateTime getArrivalTime();

        List<DepartingService> getDepartingServices();

        @Value.Lazy
        default DepartingService getFastestDepartingService() {
            return getDepartingServices().stream()
                    .sorted(Comparator.comparing(DepartingService::getTravelEndTime))
                    .findFirst()
                    .get();
        }

        @Value.Lazy
        default List<Stop> getDestinations() {
            return getDepartingServices().stream()
                    .map(DepartingService::getLastPoint)
                    .collect(toList());
        }
    }

    @Value.Immutable
    @JsonDeserialize(builder = DepartingServiceBuilder.class)
    public static interface DepartingService {

        String getName();

        String getDestination();

        List<Stop> getPoints();

        @Nullable
        LocalDateTime getDepartureTime();

        @Nullable
        ClosestFacilityRoute getFirstRoute();

        @Nullable
        LocalDateTime getArrivalTime();

        @Nullable
        ClosestFacilityRoute getSecondRoute();


        @Value.Lazy
        default LocalDateTime getTravelEndTime() {
            if (getArrivalTime()==null){
                return null;
            }
            return getArrivalTime().plusMinutes((long)getSecondRoute().getTotalTime());
        }

        @Value.Lazy
        default Stop getLastPoint() {
            return getPoints().get(getPoints().size()-1);
        }
    }
}
