package com.example.john.routing.services;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.Nullable;

import com.codepoetics.protonpack.StreamUtils;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityResult;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityRoute;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityTask;
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.esri.arcgisruntime.tasks.networkanalysis.Incident;
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

    /**
     * Constructor
     * @param ctx
     */
    public BusRoutingService(Context ctx) {
        this.res = ctx.getResources();
        this.ctx = ctx;
        this.esriService = new EsriService(ctx);
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

        String stationPairKey = Stream
                .of(
                    departureStation.name(),
                    destinationStation.name())
                .sorted()
                .collect(joining("_"));

        List<String> allowedServiceNames = servicesByStationPair.get(stationPairKey);
        Predicate<Service> allowedServiceFilter = service -> allowedServiceNames.contains(service.getName());
        Map<String,Service> allowedServices = allServices.values().stream().filter(allowedServiceFilter).collect(toMap(Service::getName, java.util.function.Function.identity()));
        List<Integer> allowedStopIds = allowedServices.values().stream().flatMap(Service::getStopIds).distinct().collect(toList());
        List<Stop> allowedStops = allStops.values().stream()
                .filter(stop -> allowedStopIds.contains(stop.getStopId()))
                .collect(toList());

        Map<Integer,Integer> stopIdToStopIndex = new HashMap<>();
        for (int i=0; i<allowedStops.size(); i++) {
            stopIdToStopIndex.put(allowedStops.get(i).getStopId(), i);
        }

        ClosestFacilityResult closestFacilityResult = esriService.getClosestFacilityResult(
                // incidents
                Stream.of(departureStation, destinationStation)
                        .map(si -> new Point(
                                si.getLon(),
                                si.getLat(),
                                SpatialReferences.getWebMercator()))
                        ::iterator,
                // facilities
                allowedStops.stream()
                        .map(s -> new Point(
                                s.getLongitude(),
                                s.getLatitude(),
                                SpatialReferences.getWebMercator()))
                        ::iterator);

        List<DepartureFacility> departures = new ArrayList<>();

        List<Stop> rankedDestinationStops = closestFacilityResult.getRankedFacilityIndexes(1).stream().map(allowedStops::get).collect(Collectors.toList());

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
        List<DepartureFacility> hydratedDepartures = departures
                .stream()
                .map(departureFacility -> {

                    ClosestFacilityRoute route  = closestFacilityResult.getRoute(departureFacility.getIndex(), 0);
                    LocalDateTime arrivalTimeToFacility = toLocalDateTime(route.getEndTime());
                    LocalTime nbf = arrivalTimeToFacility.toLocalTime(); // not before
                    Timetable departureTimetable = busService.getTimetable(departureFacility.getStop().getStopId());
                    List<DepartingService> hydratedDepartingServices = departureFacility.getDepartingServices()
                            .stream()
                            .map(departingService -> {
                                // Calculate departure time
                                LocalDateTime departureTime = nextArrival(departingService, departureTimetable, nbf);
                                // Calculate arrival time
                                Timetable destinationTimetable = busService.getTimetable(departingService.getLastPoint().getStopId());
                                LocalDateTime arrivalTime = nextArrival(departingService, destinationTimetable, nbf);
                                ClosestFacilityRoute route2  = closestFacilityResult.getRoute(stopIdToStopIndex.get(departingService.getLastPoint().getStopId()), 1);

                                return new DepartingServiceBuilder()
                                        .from(departingService)
                                        .firstRoute(route)
                                        .departureTime(departureTime)
                                        .arrivalTime(arrivalTime)
                                        .secondRoute(route2)
                                        .build();

                            })
                            .collect(toList());
                    return new DepartureFacilityBuilder()
                            .from(departureFacility)
                            .arrivalTime(arrivalTimeToFacility)
                            .departingServices(hydratedDepartingServices)
                            .build();
                })
                .collect(Collectors.toList());

        // Now we should be able to rank the departure facilities...
        DepartureFacility fastestDeparture = hydratedDepartures.stream()
                .sorted(Comparator.comparing(d -> d.getFastestDepartingService().getTravelEndTime()))
                .findFirst()
                .get();
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        LineSymbol pedestrianRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.DASH_DOT, Color.LTGRAY, 2.0f);
        LineSymbol busRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GRAY, 2.0f);

        // walk to the closest bus station
        graphicsOverlay.getGraphics().add(new Graphic(fastestDeparture.getFastestDepartingService().getFirstRoute().getRouteGeometry(), pedestrianRouteSymbol));

        // travel with the bus
        Polyline busStopGeometry = new Polyline(
                                        new PointCollection(
                                                fastestDeparture
                                                        .getFastestDepartingService()
                                                        .getPoints()
                                                        .stream()
                                                        .map(stop -> new Point(stop.getLongitude(), stop.getLatitude(), SpatialReferences.getWebMercator()))
                                                        ::iterator
                                        ));
        graphicsOverlay.getGraphics().add(new Graphic(busStopGeometry, busRouteSymbol));

        // walk from the bus station to the destination train station
        graphicsOverlay.getGraphics().add(new Graphic(fastestDeparture.getFastestDepartingService().getSecondRoute().getRouteGeometry(), pedestrianRouteSymbol));

        List<DirectionManeuver> directionManeuvers = Stream
                .concat(
                    // directions for walking to the closest bus station
                    fastestDeparture.getFastestDepartingService().getFirstRoute().getDirectionManeuvers().stream(),
                    // TODO: add custom direction maneuvers, eg. 'take bus service 12 arriving at 12:00'
                    // directions for walking from the bus station to the destination train station
                    fastestDeparture.getFastestDepartingService().getSecondRoute().getDirectionManeuvers().stream()
                )
                .collect(toList());

        return new RoutingResultBuilder()
                .graphicsOverlay(graphicsOverlay)
                .directionManeuvers(directionManeuvers)
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
                //.filter(d -> {
                //    // TODO: 0-6?
                //    int day = LocalDate.now().getDayOfWeek().ordinal();
                //    return d.getDay() == day;
                //})
                .filter(d -> LocalTime.parse(d.getTime(), dtf).isAfter(nbf))
                .findFirst()
                .get();
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
        for (Service.Route route : service.getRoutes()) {
            // start from the next point after the departure
            List<Stop> stops = StreamUtils.skipUntilInclusive(route.getPoints().stream(), point -> point.getStopId() == departure.getStopId())
                    .map(point -> allStops.get(point.getStopId()))
                    .collect(toList());

            for (Stop allowedDestination : allowedDestinations) {
                if (stops.indexOf(allowedDestination) != -1) {
                    stops = stops.subList(0, stops.indexOf(allowedDestination));
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
