package com.ex.gateways.manage.restgateways.utils;

import com.ex.gateways.manage.restgateways.dto.GatewayDto;
import com.ex.gateways.manage.restgateways.dto.PeripheralDeviceDto;
import com.ex.gateways.manage.restgateways.model.Gateway;
import com.ex.gateways.manage.restgateways.model.PeripheralDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GatewayHelper extends GatewayDataValidator {

    public List<GatewayDto> convertToGatewayDto(List<Gateway> gateways) {
        List<GatewayDto> gatewayDtos = new ArrayList<>();

        gateways.forEach(gateway -> {
            GatewayDto gatewayDto = convertToGatewayDto(gateway);
            gatewayDtos.add(gatewayDto);
        });

        return gatewayDtos;
    }

    public List<PeripheralDeviceDto> convertToPeripheralList(Set<PeripheralDevice> peripheralDeviceSet) {
        List<PeripheralDeviceDto> peripheralDeviceDtoList = new ArrayList<>();

        peripheralDeviceSet.forEach(peripheralDevice -> {
            PeripheralDeviceDto peripheralDeviceDto = convertToPeripheralDeviceDto(peripheralDevice);
            peripheralDeviceDtoList.add(peripheralDeviceDto);
        });

        return peripheralDeviceDtoList;
    }

    public GatewayDto convertToGatewayDto(Gateway gateway) {
        return new GatewayDto(gateway.getSerialNumber(), gateway.getName(), gateway.getIpAddress(), convertToPeripheralList(gateway.getPeripheralDevices()));
    }

    public PeripheralDeviceDto convertToPeripheralDeviceDto(PeripheralDevice peripheralDevice) {
        return new PeripheralDeviceDto(peripheralDevice.getUid(), peripheralDevice.getVendor(), peripheralDevice.getDateCreated(), getGatewayStatusByValue(peripheralDevice.getStatus()));
    }

    public GatewayStatus getGatewayStatusByValue(String value) {
        return GatewayStatus.valueOf(value);
    }

}
