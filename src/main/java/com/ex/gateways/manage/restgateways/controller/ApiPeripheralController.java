package com.ex.gateways.manage.restgateways.controller;

import com.ex.gateways.manage.restgateways.dto.GatewayDto;
import com.ex.gateways.manage.restgateways.dto.PeripheralDeviceDto;
import com.ex.gateways.manage.restgateways.error.GatewayException;
import com.ex.gateways.manage.restgateways.error.GatewayNotFoundException;
import com.ex.gateways.manage.restgateways.model.PeripheralDevice;
import com.ex.gateways.manage.restgateways.service.peripheral.PeripheralDeviceService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Description(value = "")
@RequestMapping(path = "/api/gateway")
@Api(value = "Devices Gateway Management", description = "Update or remove devices from a gateway")
public class ApiPeripheralController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PeripheralDeviceService peripheralDeviceService;

    @ApiOperation(value = "Add a device to gateway")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gateway updated"),
            @ApiResponse(code = 404, message = "Gateway not found"),
            @ApiResponse(code = 500, message = "Error updating gateways detail")
    })
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<GatewayDto> addDeviceToGateway(
            @ApiParam(value = "Json with device to be added to gateway", required = true)
            @RequestBody PeripheralDeviceDto peripheralDeviceDto,
            @ApiParam(value = "Gateway id that will be updated", required = true)
            @PathVariable String id) {
        try {
            PeripheralDevice peripheralDevice = convertToPeripheralDeviceEntity(peripheralDeviceDto);
            long idGateway = Long.parseLong(id);
            GatewayDto gatewayDto = peripheralDeviceService.addPeripheralDeviceToGateway(peripheralDevice, idGateway);
            return new ResponseEntity<>(gatewayDto, HttpStatus.OK);
        } catch (GatewayNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (GatewayException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete a device from a gateway")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Device updated"),
            @ApiResponse(code = 404, message = "Device not found"),
            @ApiResponse(code = 500, message = "Error deleting a device from gateway")
    })
    @DeleteMapping(path = "/delete/{idGateway}/{idPeripheral}")
    public ResponseEntity<GatewayDto> deleteGatewayDevice(
            @ApiParam(value = "Json with device to be deleted", required = true)
            @RequestBody PeripheralDeviceDto peripheralDeviceDto,
            @ApiParam(value = "Gateway id that will be updated", required = true)
            @PathVariable String idGateway,
            @ApiParam(value = "Device id that will be delete", required = true)
            @PathVariable String idPeripheral) {
        try {
            PeripheralDevice peripheralDevice = convertToPeripheralDeviceEntity(peripheralDeviceDto);
            long gatewayId = Long.parseLong(idGateway);
            long peripheralId = Long.parseLong(idPeripheral);
            GatewayDto gatewayDto = peripheralDeviceService.deletePeripheralDeviceFromGateway(peripheralDevice, gatewayId, peripheralId);
            return new ResponseEntity<>(gatewayDto, HttpStatus.OK);
        } catch (GatewayNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (GatewayException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PeripheralDevice convertToPeripheralDeviceEntity(PeripheralDeviceDto peripheralDeviceDto) {
        return modelMapper.map(peripheralDeviceDto, PeripheralDevice.class);
    }
}
