package com.example.john.routing;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.john.routing.api.RoutingResult;
import com.example.john.routing.services.BusRoutingService;
import com.example.john.config.TrainStationInfo;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BusRoutingServiceTest {

    @Test
    public void test() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        BusRoutingService service = new BusRoutingService(appContext);
        RoutingResult res = service.route(TrainStationInfo.EDB, TrainStationInfo.EDP);
        res.getGraphicsOverlay();
    }

}
