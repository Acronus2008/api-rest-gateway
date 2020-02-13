package com.ex.gateways.manage.restgateways.service.peripheral;

import com.ex.gateways.manage.restgateways.dto.GatewayDto;
import com.ex.gateways.manage.restgateways.error.GatewayException;
import com.ex.gateways.manage.restgateways.error.GatewayNotFoundException;
import com.ex.gateways.manage.restgateways.model.Gateway;
import com.ex.gateways.manage.restgateways.model.PeripheralDevice;
import com.ex.gateways.manage.restgateways.repository.GatewayRepository;
import com.ex.gateways.manage.restgateways.repository.PeripheralDeviceRepository;
import com.ex.gateways.manage.restgateways.utils.GatewayHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PeripheralDeviceService extends GatewayHelper implements IPeripheralDeviceService {

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private PeripheralDeviceRepository peripheralDeviceRepository;

    @Override
    public GatewayDto deletePeripheralDeviceFromGateway(PeripheralDevice peripheralDevice, Long idGateway, Long peripheralId) throws GatewayNotFoundException, GatewayException {
        try {

            Gateway gateway = findGatewayBeforeDeleteTheDevice(idGateway);

            PeripheralDevice deviceFound = findPeripheralDeviceBeforeDeleteFromGateway(peripheralDevice, peripheralId);

            if (!deviceFound.getUid().equals(peripheralDevice.getUid())) {
                throw new GatewayNotFoundException(String.format("Can't delete a device with different uid: Device found: %s -> Device to delete: %s", deviceFound.getUid(), peripheralDevice.getUid()));
            }

            gateway.getPeripheralDevices().remove(deviceFound);

            peripheralDeviceRepository.deleteById(deviceFound.getId());

            return convertToGatewayDto(gateway);
        } catch (Exception e) {
            throw new GatewayException(e.getMessage());
        }
    }

    @Override
    public GatewayDto addPeripheralDeviceToGateway(PeripheralDevice peripheralDevice, long idGateway) throws GatewayNotFoundException, GatewayException {
        try {
            Gateway gatewayFound = getGatewayById(idGateway).get();

            addPeripheralDeviceToGatewayFound(peripheralDevice, gatewayFound);

            validateGatewayData(gatewayFound);

            Gateway gateway = gatewayRepository.save(gatewayFound);

            return convertToGatewayDto(gateway);
        } catch (NoSuchElementException e) {
            throw new GatewayNotFoundException(String.format("There is no gateway with this id %s", idGateway));
        } catch (Exception e) {
            throw new GatewayException(e.getMessage());
        }
    }

    private PeripheralDevice findPeripheralDeviceBeforeDeleteFromGateway(PeripheralDevice peripheralDevice, Long peripheralId) throws GatewayNotFoundException {
        try {
            return getPeripheralDeviceById(peripheralId).get();
        } catch (NoSuchElementException e) {
            throw new GatewayNotFoundException(String.format("There is no peripheral device with this uid %s", peripheralDevice.getUid()));
        }
    }

    private Gateway findGatewayBeforeDeleteTheDevice(Long idGateway) throws GatewayNotFoundException {
        try {
            return getGatewayById(idGateway).get();
        } catch (NoSuchElementException e) {
            throw new GatewayNotFoundException(String.format("There is no gateway with this id %s", idGateway));
        }
    }

    private void addPeripheralDeviceToGatewayFound(PeripheralDevice peripheralDevice, Gateway gatewayFound) {
        Set<PeripheralDevice> peripheralDeviceSet = new HashSet<>();
        peripheralDevice.setGateway(gatewayFound);
        peripheralDeviceSet.add(peripheralDevice);

        gatewayFound.getPeripheralDevices().addAll(peripheralDeviceSet);
    }

    private Optional<PeripheralDevice> getPeripheralDeviceById(long peripheralId) {
        return peripheralDeviceRepository.findById(peripheralId);
    }

    private Optional<Gateway> getGatewayById(long idGateway) {
        return gatewayRepository.findById(idGateway);
    }
}
