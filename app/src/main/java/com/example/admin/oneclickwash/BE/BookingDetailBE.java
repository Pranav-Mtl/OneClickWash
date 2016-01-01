package com.example.admin.oneclickwash.BE;

import android.widget.TextView;

/**
 * Created by appslure on 16-12-2015.
 */
public class BookingDetailBE {

    private String OrderId,Name,Mobile,Address,Service,Status;
    private String PickDate,PickTime,DropTime,DropDate,Paid,Free;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPickDate() {
        return PickDate;
    }

    public void setPickDate(String pickDate) {
        PickDate = pickDate;
    }

    public String getPickTime() {
        return PickTime;
    }

    public void setPickTime(String pickTime) {
        PickTime = pickTime;
    }

    public String getFree() {
        return Free;
    }

    public void setFree(String free) {
        Free = free;
    }

    public String getPaid() {
        return Paid;
    }

    public void setPaid(String paid) {
        Paid = paid;
    }

    public String getDropDate() {
        return DropDate;
    }

    public void setDropDate(String dropDate) {
        DropDate = dropDate;
    }

    public String getDropTime() {
        return DropTime;
    }

    public void setDropTime(String dropTime) {
        DropTime = dropTime;
    }
}
