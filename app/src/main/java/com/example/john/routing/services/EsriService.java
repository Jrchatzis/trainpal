package com.example.john.routing.services;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.loadable.LoadStatus;
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
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.esri.arcgisruntime.tasks.networkanalysis.Incident;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.StreamSupport;

public class EsriService {

    private final Activity activity;
    private final Context ctx;


    public EsriService(Activity activity) {
        this.activity = activity;
        this.ctx = activity.getApplicationContext();
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
        final String URL = "http://route.arcgis.com/arcgis/rest/services/World/Route/NAServer";
        //final String URL = "http://route.arcgis.com/arcgis/rest/services/World/ClosestFacility/NAServer/ClosestFacility_World";
        //final String URL = "https://utility.arcgis.com/usrsvcs/appservices/Sxhd9iPYDp9l2QJZ/rest/services/World/ClosestFacility/GPServer/FindClosestFacilities/submitJob";

        try {
            final ClosestFacilityTask closestFacilityTask = new ClosestFacilityTask(ctx, URL);
            //UserCredential userCredential = new UserCredential("John_new", "jr9851jr9851");

            closestFacilityTask.loadAsync();
            closestFacilityTask.addDoneLoadingListener(() -> {
                if (closestFacilityTask.getLoadStatus() != LoadStatus.LOADED) {
                    Log.e(EsriService.class.getSimpleName(),"got load status " + closestFacilityTask.getLoadStatus());
                    closestFacilityTask.getLoadError().printStackTrace();
                    result.completeExceptionally(closestFacilityTask.getLoadError());
                    return;
                }
                Log.d(EsriService.class.getSimpleName(), "Loaded " + closestFacilityTask.getLoadStatus());
                ListenableFuture<ClosestFacilityParameters> closestFacilityParametersFuture = closestFacilityTask.createDefaultParametersAsync();
                closestFacilityParametersFuture.addDoneListener(() -> {
                    try {
                        Log.d(EsriService.class.getSimpleName(), "done closestFacilityParametersFuture");
                        ClosestFacilityParameters closestFacilityParameters = closestFacilityParametersFuture.get();
                        closestFacilityParameters.setReturnDirections(true);
                        closestFacilityParameters.setReturnRoutes(true);
                        closestFacilityParameters.setIncidents(StreamSupport.stream(incidents.spliterator(), false).map(Incident::new)::iterator);
                        closestFacilityParameters.setFacilities(StreamSupport.stream(facilities.spliterator(), false).map(Facility::new)::iterator);

                        ListenableFuture<ClosestFacilityResult> closestFacilityResultFuture = closestFacilityTask.solveClosestFacilityAsync(closestFacilityParameters);
                        closestFacilityResultFuture.addDoneListener(() -> {
                            try {
                                Log.d(EsriService.class.getSimpleName(), "done closestFacilityResultFuture");
                                // here's the result
                                result.complete(closestFacilityResultFuture.get());
                            } catch (InterruptedException | ExecutionException e) {
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
            throw new RuntimeException(e);
        }
    }
}
