package com.example.john.tfe;

import android.content.res.Resources;

import com.example.john.travelinfo.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class TfeOpenDataServiceLocal implements TfeOpenDataService {

    private ObjectMapper om = new ObjectMapper();
    private final Resources res;

    public TfeOpenDataServiceLocal(Resources res) {
        this.res = res;
    }

    @Override
    public Stops getStops() {
        return readJson(R.raw.tfe_stops, Stops.class);
    }

    @Override
    public Services getServices() {
        return readJson(R.raw.tfe_services, Services.class);
    }

    @Override
    public Timetable getTimetable(int stopId) {
        String fieldName = String.format("tfe_tt_%i", stopId);
        try {
            int resourceId = R.raw.class.getDeclaredField(fieldName).getInt(null);
            return readJson(resourceId, Timetable.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T readJson(int resourceId, Class<T> clazz) {
        try (InputStream is = res.openRawResource(resourceId)) {
            return om.readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
