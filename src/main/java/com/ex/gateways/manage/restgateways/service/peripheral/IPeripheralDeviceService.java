package com.ex.gateways.manage.restgateways.service.peripheral;

import com.ex.gateways.manage.restgateways.dto.GatewayDto;
import com.ex.gateways.manage.restgateways.error.GatewayException;
import com.ex.gateways.manage.restgateways.error.GatewayNotFoundException;
import com.ex.gateways.manage.restgateways.model.PeripheralDevice;

public interface IPeripheralDeviceService {
    GatewayDto deletePeripheralDeviceFromGateway(PeripheralDevice peripheralDevice, Long idGateway, Long peripheralId) throws GatewayNotFoundException, GatewayException;

    GatewayDto addPeripheralDeviceToGateway(PeripheralDevice peripheralDevice, long idGateway) throws GatewayNotFoundException, GatewayException;
}
