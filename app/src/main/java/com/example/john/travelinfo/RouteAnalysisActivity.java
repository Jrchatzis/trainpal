package com.example.john.travelinfo;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.example.john.config.TrainStationInfo;
import com.example.john.routing.api.Direction;
import com.example.john.routing.services.BusRoutingService;
import com.example.john.routing.api.RoutingResult;
import com.example.john.routing.api.RoutingService;
import com.example.john.routing.services.EsriService;
import com.example.john.routing.services.TaxiRoutingService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import android.widget.CheckBox;

public class RouteAnalysisActivity extends AppCompatActivity {

    private TrainStationInfo departure;
    private TrainStationInfo destination;
    private Timer timer;
    private MapView mMapView;
    private LocationDisplay mLocationDisplay;
    private Spinner mSpinner;
    private CheckBox checkbox;
    private CheckBox checkbox2;
    private TextView service;
    private TextView dropOffStop;
    private TextView taxiArrival;



    private int requestCode = 2;
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private EsriService esriService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_analysis);
        Bundle bundle = getIntent().getExtras();
        departure = (TrainStationInfo)bundle.get("departure");
        destination = (TrainStationInfo)bundle.get("destination");

        // Get the Spinner from layout
        mSpinner = (Spinner) findViewById(R.id.spinner);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            int requestCode = 2;
            ActivityCompat.requestPermissions(RouteAnalysisActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }

        mMapView = (MapView) findViewById(R.id.mapView);
        // get the MapView's LocationDisplay
        mLocationDisplay = mMapView.getLocationDisplay();

        esriService = new EsriService(this);
        esriService.getMobileMapPackage().thenAccept(mmpk -> {
            runOnUiThread(() ->{
                ArcGISMap navigationMap = mmpk.getMaps().get(0);
                mMapView.setMap(navigationMap);
                Log.i(RouteAnalysisActivity.class.getSimpleName(), "added test overlay");
                //addTestOverlay();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        drawAlternativeRoute(new TaxiRoutingService(RouteAnalysisActivity.this, esriService));
                        drawAlternativeRoute(new BusRoutingService(RouteAnalysisActivity.this, esriService));
                    }
                }, 0L);

            });
        });

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

        Activity activity = this;
        timer = new Timer();


        checkbox = (CheckBox) findViewById(R.id.checkBox);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true) {
                    mMapView.getGraphicsOverlays().get(1).setVisible(true);
                } else {
                    mMapView.getGraphicsOverlays().get(1).setVisible(false);
                }
            }
        });


        checkbox2 = (CheckBox) findViewById(R.id.checkBox2);

        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true) {
                    mMapView.getGraphicsOverlays().get(0).setVisible(true);
                } else {
                    mMapView.getGraphicsOverlays().get(0).setVisible(false);
                }
            }
        });
    }

    private void addTestOverlay() {
        GraphicsOverlay testOverlay = new GraphicsOverlay();
        LineSymbol busRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GRAY, 2.0f);

        Point point1 = new Point(departure.getLon(),departure.getLat(), SpatialReferences.getWgs84());
        Point point2 = new Point(destination.getLon(),destination.getLat(), SpatialReferences.getWgs84());
        // travel with the bus
        Polyline busStopGeometry = new Polyline(new PointCollection(Stream.of(point1, point2).collect(Collectors.toList())));
        testOverlay.getGraphics().add(new Graphic(busStopGeometry, busRouteSymbol));
        mMapView.getGraphicsOverlays().add(testOverlay);
        mMapView.setViewpointGeometryAsync(busStopGeometry, 10);
        Log.i(RouteAnalysisActivity.class.getSimpleName(), "added test overlay");
    }

    private void drawAlternativeRoute(RoutingService routingService) {
        try {

            RoutingResult route = routingService.route(departure, destination);
            List<Direction> busDirections = route.getDirections().stream()
                    .filter(d -> d.getMode().equals("bus"))
                    .collect(Collectors.toList());


            List<Direction> taxiDirections = route.getDirections().stream()
                    .filter(d -> d.getMode().equals("taxi"))
                    .collect(Collectors.toList());

            if (route != null) {
                runOnUiThread(() -> {
                    mMapView.setViewpointGeometryAsync(route.getFullExtent(), 10);
                    mMapView.getGraphicsOverlays().add(route.getGraphicsOverlay());
                    if (!busDirections.isEmpty()) {
                        String busDepartureDirection = busDirections.get(0).getDescription();
                        String dropoffDirection = busDirections.get(1).getDescription();

                        // TODO: update the textviews or whatever!
                        TextView service = findViewById(R.id.service);
                        service.setText(busDepartureDirection);
                        TextView dropOffStop = findViewById(R.id.dropOffStop);
                        dropOffStop.setText(dropoffDirection);

                        String taxiArrival = taxiDirections.get(0).getDescription();
                        TextView taxi = findViewById(R.id.taxiArrival);
                        taxi.setText(taxiArrival);
                    }
                });
            }
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
