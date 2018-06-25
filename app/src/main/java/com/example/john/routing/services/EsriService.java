package com.example.john.routing.services;

import android.content.Context;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityResult;
import com.esri.arcgisruntime.tasks.networkanalysis.ClosestFacilityTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Facility;
import com.esri.arcgisruntime.tasks.networkanalysis.Incident;

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
            closestFacilityTask.addDoneLoadingListener(() -> loaded.set(true));
            closestFacilityTask.loadAsync();
            while (!loaded.get()) {
                Thread.sleep(100);
            }
            ClosestFacilityParameters closestFacilityParameters = closestFacilityTask.createDefaultParametersAsync().get();
            closestFacilityParameters.setReturnDirections(true);
            closestFacilityParameters.setReturnRoutes(true);
            //closestFacilityParameters.setTravelMode(TravelMode.createFromInternal(CoreTravelMode.a(5l)));

            closestFacilityParameters.setIncidents(StreamSupport.stream(incidents.spliterator(), false).map(Incident::new)::iterator);
            closestFacilityParameters.setFacilities(StreamSupport.stream(facilities.spliterator(), false).map(Facility::new)::iterator);
            return closestFacilityTask.solveClosestFacilityAsync(closestFacilityParameters).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
