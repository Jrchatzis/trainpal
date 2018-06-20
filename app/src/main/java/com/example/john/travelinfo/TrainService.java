package com.example.john.travelinfo;

import java.io.Serializable;

public class TrainService implements Serializable {

    private String delayReason;
    private String id;
    private TimeString sta;
    private String eta;
    private String ata;


    public void setAta(String ata) {
        this.ata = ata;
    }

    public String getAta() {
        return ata;
    }

    public void setSta(TimeString sta) {

        this.sta = sta;
    }

    public TimeString getSta() {

        return sta;
    }

    public void setEta(String eta) {

        this.eta = eta;
    }

    public String getEta() {

        return eta;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getId() {

        return id;
    }

    public boolean isArrived() {
        return this.ata != null && !this.ata.isEmpty();
    }

    public boolean isDelayed() {
        return this.delayReason != null && !this.delayReason.isEmpty();
    }

    public void setDelayReason(String delayReason) {
        this.delayReason = delayReason;
    }

    public String getDelayReason() {
        return delayReason;
    }

    @Override
    public String toString() {

        return id + ": " + sta;
    }
}
