package com.example.admin.oneclickwash.BE;

import java.io.Serializable;

/**
 * Created by appslure on 15-12-2015.
 */
public class PlaceOrderBE implements Serializable {
    private String date;
    private String timeSlot;
    private boolean dryClean;
    private String dryCleanCloth;
    private String slotID;

    public String getSlotID() {
        return slotID;
    }

    public void setSlotID(String slotID) {
        this.slotID = slotID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public boolean isDryClean() {
        return dryClean;
    }

    public void setDryClean(boolean dryClean) {
        this.dryClean = dryClean;
    }

    public String getDryCleanCloth() {
        return dryCleanCloth;
    }

    public void setDryCleanCloth(String dryCleanCloth) {
        this.dryCleanCloth = dryCleanCloth;
    }
}
