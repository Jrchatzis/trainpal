package com.example.john.routing.services;

import android.content.Context;

import android.util.Log;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityResult;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.esri.arcgisruntime.tasks.networkanalysis.Incident;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.StreamSupport;

public class EsriService {

    public Context ctx;

    public EsriService(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Use ESRI's online service to calculate the closest facility
     *
     * @param incidents the incidents
     * @param facilities the facilities
     * @return the closest facility result
     */
    public ClosestFacilityResult getClosestFacilityResult(Iterable<Point> incidents, Iterable<Point> facilities) {
        final String URL = "http://sampleserver6.arcgisonline.com/arcgis/rest/services/NetworkAnalysis/SanDiego/NAServer/ClosestFacility";
        try {
            final ClosestFacilityTask closestFacilityTask = new ClosestFacilityTask(ctx, URL);
            AtomicBoolean loaded = new AtomicBoolean(false);
            closestFacilityTask.addDoneLoadingListener(() -> {
                loaded.set(true);
            });
            closestFacilityTask.loadAsync();
            while (!loaded.get()) {
                Thread.sleep(100);
            }
            if (closestFacilityTask.getLoadStatus() != LoadStatus.LOADED) {
                throw new RuntimeException("got load status " + closestFacilityTask.getLoadStatus());
            } else {
                Log.d("facilityTask", "Loaded " + closestFacilityTask.getLoadStatus());
                ListenableFuture<ClosestFacilityParameters> closestFacilityParametersFuture = closestFacilityTask.createDefaultParametersAsync();

                closestFacilityParametersFuture.addDoneListener(() -> {
                    try {
                        ClosestFacilityParameters closestFacilityParameters = closestFacilityParametersFuture.get();
                        closestFacilityParameters.setReturnDirections(true);
                        closestFacilityParameters.setReturnRoutes(true);

                        closestFacilityParameters.setIncidents(StreamSupport.stream(incidents.spliterator(), false).map(Incident::new)::iterator);
                        closestFacilityParameters.setFacilities(StreamSupport.stream(facilities.spliterator(), false).map(Facility::new)::iterator);

                        ListenableFuture<ClosestFacilityResult> closestFacilityResultFuture = closestFacilityTask.solveClosestFacilityAsync(closestFacilityParameters);
                        closestFacilityResultFuture.addDoneListener(() -> {
                            try {

                                // here's the result
                                ClosestFacilityResult closestFacilityResult = closestFacilityResultFuture.get();

                                Log.d("result", closestFacilityResult.getFacilities().get(0).getName());
                            } catch (InterruptedException | ExecutionException e) {
                                Log.e("facilityResult", e.getMessage());
                            }
                        });
                    } catch (InterruptedException | ExecutionException e) {
                        Log.e("facilityParameters", e.getMessage());
                    }
                });

            }
        } catch (Exception e) {
            e.getCause().printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
}
