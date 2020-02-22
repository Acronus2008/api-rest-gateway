package com.ex.gateways.manage.restgateways.service.gateway;

import com.ex.gateways.manage.restgateways.dto.GatewayDto;
import com.ex.gateways.manage.restgateways.dto.event.MessageEventDto;
import com.ex.gateways.manage.restgateways.error.GatewaySameSerialNumberException;
import com.ex.gateways.manage.restgateways.utils.MessageEventType;
import com.ex.gateways.manage.restgateways.error.GatewayException;
import com.ex.gateways.manage.restgateways.error.GatewayNotFoundException;
import com.ex.gateways.manage.restgateways.error.GatewaySameIpAddressException;
import com.ex.gateways.manage.restgateways.model.Gateway;
import com.ex.gateways.manage.restgateways.repository.GatewayRepository;
import com.ex.gateways.manage.restgateways.utils.GatewayHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class GatewayService extends GatewayHelper implements IGatewayService {

    @Autowired
    private GatewayRepository gatewayRepository;

    @Override
    public MessageEventDto addGateway(Gateway gateway) throws GatewaySameIpAddressException, GatewaySameSerialNumberException, GatewayException {
        try {
            validateGatewayData(gateway);

            if (existGatewayWithSameAddress(gateway.getIpAddress())) {
                throw new GatewaySameIpAddressException(String.format("A gateway with the same ip address:%s exist", gateway.getIpAddress()));
            }

            if (existGatewayWithSameSerialNumber(gateway.getSerialNumber())) {
                throw new GatewaySameSerialNumberException(String.format("A gateway with the same serial number:%s exist", gateway.getSerialNumber()));
            }

            setGatewayToPeripheralDevicesBeforeCreate(gateway);

            gatewayRepository.save(gateway);

            return new MessageEventDto(String.format("Gateway created name:%s ip:%s serial number:%s devices:%s", gateway.getName(), gateway.getIpAddress(), gateway.getSerialNumber(), gateway.getPeripheralDevices().size()), MessageEventType.INFO, new Date());
        } catch (Exception e) {
            throw new GatewayException(String.format("An error occurred adding a gateway: %s", e.getMessage()));
        }
    }

    @Override
    public List<GatewayDto> getAllGateways() {
        try {
            List<Gateway> gateways = gatewayRepository.findAll();
            return convertToGatewayDto(gateways);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public GatewayDto getGatewayDetails(long idGateway) throws GatewayException, GatewayNotFoundException {
        try {
            Gateway gateway = getGatewayById(idGateway).get();
            return convertToGatewayDto(gateway);
        } catch (NoSuchElementException e) {
            throw new GatewayNotFoundException(String.format("There is no gateway with this id %s", idGateway));
        } catch (Exception e) {
            throw new GatewayException(e.getMessage());
        }
    }



    private boolean existGatewayWithSameSerialNumber(String serialNumber) {
        try {
            gatewayRepository.findGatewayBySerialNumber(serialNumber).get();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean existGatewayWithSameAddress(String ipAddress) {
        try {
            gatewayRepository.findGatewayByIpAddress(ipAddress).get();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private Optional<Gateway> getGatewayById(long idGateway) {
        return gatewayRepository.findById(idGateway);
    }

    private void setGatewayToPeripheralDevicesBeforeCreate(Gateway gateway) {
        gateway.getPeripheralDevices().forEach(peripheralDevice -> {
            peripheralDevice.setGateway(gateway);
        });
    }
}
