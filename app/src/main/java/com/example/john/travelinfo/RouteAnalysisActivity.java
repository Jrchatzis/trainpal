package com.example.john.travelinfo;

import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.example.john.routing.BusRoutingService;
import com.example.john.routing.RoutingResult;
import com.example.john.routing.RoutingService;
import com.example.john.routing.TaxiRoutingService;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RouteAnalysisActivity extends AppCompatActivity {

    private TrainStationInfo departure;
    private TrainStationInfo destination;
    private Timer timer;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;
    private Spinner mSpinner;

    private int requestCode = 2;
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_analysis);
        Bundle bundle = getIntent().getExtras();
        departure = (TrainStationInfo)bundle.get("departure");
        destination = (TrainStationInfo)bundle.get("destination");

        //AlertDialog.Builder builderDelay = new AlertDialog.Builder(RouteAnalysisActivity.this);
        //builderDelay.setCancelable(true);
        //builderDelay.setTitle("Mode of transportation decision");
        //builderDelay.setMessage("Which mode of transportation, apart from walking, would you like to use?");
        //builderDelay.setPositiveButton("Bus",
                //new DialogInterface.OnClickListener() {
                    //@Override
                    //public void onClick(DialogInterface dialog, int which) {
                    //}
                //});
        //builderDelay.setNegativeButton("Taxi",
                //new DialogInterface.OnClickListener() {
                    //@Override
                    //public void onClick(DialogInterface dialog, int which) {
                    //}
                //});
        //builderDelay.create().show();

// Get the Spinner from layout
        mSpinner = (Spinner) findViewById(R.id.spinner);

        // Get the MapView from layout and set a map with the BasemapType Imagery
        mMapView = (MapView) findViewById(R.id.mapView);
        ArcGISMap mMap = new ArcGISMap(Basemap.createImagery());
        mMapView.setMap(mMap);

        // get the MapView's LocationDisplay
        mLocationDisplay = mMapView.getLocationDisplay();

        // Open a StreetMap Premium mobile map package
        //MobileMapPackage mobileMapPackage = new MobileMapPackage("../../../../../United_Kingdom.mmpk");


        //mobileMapPackage.addDoneLoadingListener(() -> {
            //if (mobileMapPackage.getLoadStatus() == LoadStatus.LOADED && mobileMapPackage.getMaps().size() > 0) {
                // Get the first map, which is the navigation map
                //ArcGISMap navigationMap = mobileMapPackage.getMaps().get(0);


                // Add it to the map view
                //mMapView.setMap(navigationMap);
            //}
        //});


        // Listen to changes in the status of the location data source.
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK, then continue.
                if (dataSourceStatusChangedEvent.isStarted())
                    return;

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null)
                    return;

                // If an error is found, handle the failure to start.
                // Check permissions to see if failure may be due to lack of permissions.
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(RouteAnalysisActivity.this, reqPermissions[0]) ==
                        PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(RouteAnalysisActivity.this, reqPermissions[1]) ==
                        PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1 && permissionCheck2)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(RouteAnalysisActivity.this, reqPermissions, requestCode);
                } else {
                    // Report other unknown failure types to the user - for example, location services may not
                    // be enabled on the device.
                    String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(RouteAnalysisActivity.this, message, Toast.LENGTH_LONG).show();

                    // Update UI to reflect that the location display did not actually start
                    mSpinner.setSelection(0, true);
                }
            }
        });


        // Populate the list for the Location display options for the spinner's Adapter
        ArrayList<ItemData> list = new ArrayList<>();
        list.add(new ItemData("Stop", R.drawable.locationdisplaydisabled));
        list.add(new ItemData("On", R.drawable.locationdisplayon));
        list.add(new ItemData("Re-Center", R.drawable.locationdisplayrecenter));
        list.add(new ItemData("Navigation", R.drawable.locationdisplaynavigation));
        list.add(new ItemData("Compass", R.drawable.locationdisplayheading));

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        // Stop Location Display
                        if (mLocationDisplay.isStarted())
                            mLocationDisplay.stop();
                        break;
                    case 1:
                        // Start Location Display
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                    case 2:
                        // Re-Center MapView on Location
                        // AutoPanMode - Default: In this mode, the MapView attempts to keep the location symbol on-screen by
                        // re-centering the location symbol when the symbol moves outside a "wander extent". The location symbol
                        // may move freely within the wander extent, but as soon as the symbol exits the wander extent, the MapView
                        // re-centers the map on the symbol.
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                    case 3:
                        // Start Navigation Mode
                        // This mode is best suited for in-vehicle navigation.
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                    case 4:
                        // Start Compass Mode
                        // This mode is better suited for waypoint navigation when the user is walking.
                        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        if (!mLocationDisplay.isStarted())
                            mLocationDisplay.startAsync();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                drawAlternativeRoute(new BusRoutingService(getResources(), getApplicationContext()));
            }
        }, 0L);

        /*
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                drawAlternativeRoute(new TaxiRoutingService(getResources(), getApplicationContext()));
            }
        }, 0L);
        */

    }

    private void drawAlternativeRoute(RoutingService routingService) {
        // TODO: find departure and destination Stations from previous screens
        try {
            RoutingResult route = routingService.route(departure, destination);
            runOnUiThread(()->{
                SimpleLineSymbol routeSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 2.0f);

                // add a new graphic to display the route (in a graphics overlay)
                GraphicsOverlay resultOverlay = new GraphicsOverlay();
                resultOverlay.getGraphics().add(new Graphic(route.getRoute().getRouteGeometry(), routeSymbol));

                // add the new graphic to a map view
                mMapView.getGraphicsOverlays().add(resultOverlay);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Location permission was granted. This would have been triggered in response to failing to start the
            // LocationDisplay, so try starting this again.
            mLocationDisplay.startAsync();
        } else {
            // If permission was denied, show toast to inform user what was chosen. If LocationDisplay is started again,
            // request permission UX will be shown again, option should be shown to allow never showing the UX again.
            // Alternative would be to disable functionality so request is not shown again.
            Toast.makeText(RouteAnalysisActivity.this, getResources().getString(R.string.location_permission_denied), Toast
                    .LENGTH_SHORT).show();

            // Update UI to reflect that the location display did not actually start
            mSpinner.setSelection(0, true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }
}
