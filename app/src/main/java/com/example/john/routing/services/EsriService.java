package com.example.john.routing.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.TransportationNetworkDataset;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.security.AuthenticationChallenge;
import com.esri.arcgisruntime.security.AuthenticationChallengeHandler;
import com.esri.arcgisruntime.security.AuthenticationChallengeResponse;
import com.esri.arcgisruntime.security.AuthenticationManager;
import com.esri.arcgisruntime.security.Credential;
import com.esri.arcgisruntime.security.DefaultAuthenticationChallengeHandler;
import com.esri.arcgisruntime.security.OAuthConfiguration;
import com.esri.arcgisruntime.security.OAuthTokenCredential;
import com.esri.arcgisruntime.security.UserCredential;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityResult;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityTask;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityTaskInfo;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.esri.arcgisruntime.tasks.networkanalysis.Incident;
import com.esri.arcgisruntime.tasks.networkanalysis.TravelDirection;
import com.esri.arcgisruntime.tasks.networkanalysis.TravelMode;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
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
                                if (closestFacilityResult.getRankedFacilityIndexes(0).size() > 0 && closestFacilityResult.getRoute(0,0) == null) {
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

    private String getMmpkPath() {
        File f = new File("/mnt/user/0/primary/Download/United_Kingdom.mmpk");
        if (!f.exists()) {
            throw new RuntimeException("file does not exist " + f);
        }
        return f.getAbsolutePath();
    }
}
