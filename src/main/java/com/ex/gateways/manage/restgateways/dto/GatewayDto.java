package com.ex.gateways.manage.restgateways.dto;

import java.io.Serializable;
import java.util.List;

public class GatewayDto implements Serializable {
    private String serialNumber;
    private String name;
    private String ipAddress;
    private List<PeripheralDeviceDto> peripheralDevices;

    public GatewayDto(String serialNumber, String name, String ipAddress, List<PeripheralDeviceDto> peripheralDevices) {
        setSerialNumber(serialNumber);
        setName(name);
        setIpAddress(ipAddress);
        setPeripheralDevices(peripheralDevices);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public List<PeripheralDeviceDto> getPeripheralDevices() {
        return peripheralDevices;
    }

    private void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    private void setPeripheralDevices(List<PeripheralDeviceDto> peripheralDevices) {
        this.peripheralDevices = peripheralDevices;
    }
}
