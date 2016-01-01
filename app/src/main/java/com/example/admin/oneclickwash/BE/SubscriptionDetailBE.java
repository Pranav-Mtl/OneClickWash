package com.example.admin.oneclickwash.BE;

/**
 * Created by appslure on 17-12-2015.
 */
public class SubscriptionDetailBE {
    private String startDate;
    private String endDate;
    private String totalPickup;
    private String remainingPickup;
    private String totalCloth;
    private String remainingCloth;
    private String freeCloth;
    private String packageName;
    private String packageAmount;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageAmount() {
        return packageAmount;
    }

    public void setPackageAmount(String packageAmount) {
        this.packageAmount = packageAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTotalPickup() {
        return totalPickup;
    }

    public void setTotalPickup(String totalPickup) {
        this.totalPickup = totalPickup;
    }

    public String getRemainingPickup() {
        return remainingPickup;
    }

    public void setRemainingPickup(String remainingPickup) {
        this.remainingPickup = remainingPickup;
    }

    public String getTotalCloth() {
        return totalCloth;
    }

    public void setTotalCloth(String totalCloth) {
        this.totalCloth = totalCloth;
    }

    public String getRemainingCloth() {
        return remainingCloth;
    }

    public void setRemainingCloth(String remainingCloth) {
        this.remainingCloth = remainingCloth;
    }

    public String getFreeCloth() {
        return freeCloth;
    }

    public void setFreeCloth(String freeCloth) {
        this.freeCloth = freeCloth;
    }
}
