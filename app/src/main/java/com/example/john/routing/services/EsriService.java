package com.example.john.routing.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.TransportationNetworkDataset;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.security.AuthenticationManager;
import com.esri.arcgisruntime.security.DefaultAuthenticationChallengeHandler;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityResult;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.esri.arcgisruntime.tasks.networkanalysis.Incident;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.esri.arcgisruntime.tasks.networkanalysis.TravelDirection;
import com.esri.arcgisruntime.tasks.networkanalysis.TravelMode;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

public class EsriService {

    private final Activity activity;
    private final Context ctx;
    private CompletableFuture<MobileMapPackage> mmpk;


    public EsriService(Activity activity) {
        this.activity = activity;
        this.ctx = activity.getApplicationContext();
        this.mmpk = new CompletableFuture<>();

        MobileMapPackage mmpk = new MobileMapPackage(getMmpkPath());
        mmpk.loadAsync();
        mmpk.addDoneLoadingListener(() -> {
            if (mmpk.getLoadStatus() == LoadStatus.LOADED) {
                this.mmpk.complete(mmpk);
            } else {
                this.mmpk.completeExceptionally(mmpk.getLoadError());
            }
        });
    }

    public CompletableFuture<MobileMapPackage> getMobileMapPackage() {
        return mmpk;
    }

    /**
     * Use ESRI's online service to calculate the closest facility
     *
     * @param incidents the incidents
     * @param facilities the facilities
     * @return the closest facility result
     */
    public ClosestFacilityResult getClosestFacilityResult(Iterable<Point> incidents, Iterable<Point> facilities) {

        AuthenticationManager.setAuthenticationChallengeHandler(new DefaultAuthenticationChallengeHandler(activity));
        CompletableFuture<ClosestFacilityResult> result = new CompletableFuture<>();

        try {
            CompletableFuture<TransportationNetworkDataset> transportationNetwork = new CompletableFuture<>();
            List<ArcGISMap> maps = mmpk.get().getMaps();
            List<TransportationNetworkDataset> transportationNetworks = maps.get(0).getTransportationNetworks();
            transportationNetwork.complete(transportationNetworks.get(0));

            final ClosestFacilityTask closestFacilityTask = new ClosestFacilityTask(ctx, transportationNetwork.get());

            closestFacilityTask.loadAsync();
            closestFacilityTask.addDoneLoadingListener(() -> {
                if (closestFacilityTask.getLoadStatus() != LoadStatus.LOADED) {
                    Log.e(EsriService.class.getSimpleName(),"got load status " + closestFacilityTask.getLoadStatus());
                    result.completeExceptionally(closestFacilityTask.getLoadError());
                    return;
                }
                Log.d(EsriService.class.getSimpleName(), "Loaded " + closestFacilityTask.getLoadStatus());
                ListenableFuture<ClosestFacilityParameters> closestFacilityParametersFuture = closestFacilityTask.createDefaultParametersAsync();
                closestFacilityParametersFuture.addDoneListener(() -> {
                    try {
                        Log.d(EsriService.class.getSimpleName(), "done closestFacilityParametersFuture");
                        ClosestFacilityParameters closestFacilityParameters = closestFacilityParametersFuture.get();
                        closestFacilityParameters.setDefaultTargetFacilityCount(5);
                        closestFacilityParameters.setTravelDirection(TravelDirection.TO_FACILITY);
                        TravelMode travelMode = closestFacilityTask.getClosestFacilityTaskInfo().getTravelModes()
                                .stream()
                                .filter(tm -> tm.getName().equalsIgnoreCase("walking time"))
                                .findFirst()
                                .get();
                        closestFacilityParameters.setOutputSpatialReference(SpatialReferences.getWgs84());
                        closestFacilityParameters.setStartTime(Calendar.getInstance());
                        closestFacilityParameters.setTravelMode(travelMode);
                        closestFacilityParameters.setReturnDirections(true);
                        closestFacilityParameters.setReturnRoutes(true);
                        closestFacilityParameters.setIncidents(StreamSupport.stream(incidents.spliterator(), false).map(Incident::new)::iterator);
                        closestFacilityParameters.setFacilities(StreamSupport.stream(facilities.spliterator(), false).map(Facility::new)::iterator);

                        ListenableFuture<ClosestFacilityResult> closestFacilityResultFuture = closestFacilityTask.solveClosestFacilityAsync(closestFacilityParameters);
                        closestFacilityResultFuture.addDoneListener(() -> {
                            try {
                                Log.d(EsriService.class.getSimpleName(), "done closestFacilityResultFuture");
                                ClosestFacilityResult closestFacilityResult = closestFacilityResultFuture.get();
                                List<Integer> rankedFacilityIndexes = closestFacilityResult.getRankedFacilityIndexes(0);
                                if (rankedFacilityIndexes.size() > 0 && closestFacilityResult.getRoute(rankedFacilityIndexes.get(0),0) == null) {
                                    throw  new IllegalStateException("null route");
                                }
                                result.complete(closestFacilityResult);
                            } catch (Exception e) {
                                Log.e("facilityResult", e.getMessage());
                                result.completeExceptionally(e);
                            }
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        Log.e("facilityParameters", e.getMessage());
                        result.completeExceptionally(e);
                    }
                });
            });
            return result.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Use ESRI's online service to calculate a route
     * @param startTime the start time
     * @param travelMode the travel mode
     * @param start the starting point
     * @param end the ending point
     * @return the route result
     */
    public RouteResult getRouteResult(Calendar startTime, String travelMode, Point start, Point end) {


        CompletableFuture<RouteResult> result = new CompletableFuture<>();

        try {
            CompletableFuture<TransportationNetworkDataset> transportationNetwork = new CompletableFuture<>();
            List<ArcGISMap> maps = mmpk.get().getMaps();
            List<TransportationNetworkDataset> transportationNetworks = maps.get(0).getTransportationNetworks();
            transportationNetwork.complete(transportationNetworks.get(0));

            RouteTask routeTask = new RouteTask(ctx, transportationNetwork.get());
            routeTask.loadAsync();
            routeTask.addDoneLoadingListener(() -> {
                if (routeTask.getLoadStatus() != LoadStatus.LOADED) {
                    Log.e(EsriService.class.getSimpleName(),"got load status " + routeTask.getLoadStatus());
                    result.completeExceptionally(routeTask.getLoadError());
                    return;
                }
                Log.d(EsriService.class.getSimpleName(), "Loaded " + routeTask.getLoadStatus());
                ListenableFuture<RouteParameters> defaultParametersAsync = routeTask.createDefaultParametersAsync();
                defaultParametersAsync.addDoneListener(()->{
                    try {
                        RouteParameters routeParameters = defaultParametersAsync.get();
                        routeParameters.setOutputSpatialReference(SpatialReferences.getWgs84());
                        routeParameters.setStartTime(startTime);
                        TravelMode travelMode_ = routeTask.getRouteTaskInfo().getTravelModes()
                                .stream()
                                .filter(tm -> tm.getName().equalsIgnoreCase(travelMode))
                                .findFirst()
                                .get();
                        routeParameters.setTravelMode(travelMode_);
                        routeParameters.setStops(Arrays.asList(new Stop(start), new Stop(end)));
                        ListenableFuture<RouteResult> routeResultListenableFuture = routeTask.solveRouteAsync(routeParameters);
                        routeResultListenableFuture.addDoneListener(() -> {
                            Log.d(EsriService.class.getSimpleName(), "done routeResultListenableFuture");
                            try {
                                result.complete(routeResultListenableFuture.get());
                            } catch (Exception e) {
                                result.completeExceptionally(e);
                            }
                        });

                    } catch (Exception e) {
                        Log.e("routeParameters", e.getMessage());
                        result.completeExceptionally(e);
                    }
                });
            });
            return result.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getMmpkPath() {
        File f = new File("/mnt/user/0/primary/Download/United_Kingdom.mmpk");
        if (!f.exists()) {
            throw new RuntimeException("file does not exist " + f);
        }
        return f.getAbsolutePath();
    }
}
