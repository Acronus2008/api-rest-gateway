package com.ex.gateways.manage.restgateways.dto;

import com.ex.gateways.manage.restgateways.utils.GatewayStatus;

import java.io.Serializable;
import java.util.Date;

public class PeripheralDeviceDto implements Serializable {

    private long uid;
    private String vendor;
    private Date dateCreated;
    private GatewayStatus status;

    public PeripheralDeviceDto(long uid, String vendor, Date dateCreated, GatewayStatus status) {
        setUid(uid);
        setVendor(vendor);
        setDateCreated(dateCreated);
        setStatus(status);
    }

    public long getUid() {
        return uid;
    }

    public String getVendor() {
        return vendor;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public GatewayStatus getStatus() {
        return status;
    }

    private void setUid(long uid) {
        this.uid = uid;
    }

    private void setVendor(String vendor) {
        this.vendor = vendor;
    }

    private void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    private void setStatus(GatewayStatus status) {
        this.status = status;
    }

}
