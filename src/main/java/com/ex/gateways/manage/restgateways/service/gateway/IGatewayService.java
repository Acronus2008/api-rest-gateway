package com.ex.gateways.manage.restgateways.service.gateway;

import com.ex.gateways.manage.restgateways.dto.GatewayDto;
import com.ex.gateways.manage.restgateways.dto.event.MessageEventDto;
import com.ex.gateways.manage.restgateways.error.GatewayException;
import com.ex.gateways.manage.restgateways.error.GatewayNotFoundException;
import com.ex.gateways.manage.restgateways.error.GatewaySameIpAddressException;
import com.ex.gateways.manage.restgateways.model.Gateway;

import java.util.List;

public interface IGatewayService {
    MessageEventDto addGateway(Gateway gateway) throws GatewaySameIpAddressException, GatewayException;

    List<GatewayDto> getAllGateways();

    GatewayDto getGatewayDetails(long idGateway) throws GatewayException, GatewayNotFoundException;
}
