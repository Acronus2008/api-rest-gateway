package com.ex.gateways.manage.restgateways.controller;

import com.ex.gateways.manage.restgateways.dto.GatewayDto;
import com.ex.gateways.manage.restgateways.dto.event.MessageEventDto;
import com.ex.gateways.manage.restgateways.utils.MessageEventType;
import com.ex.gateways.manage.restgateways.error.GatewayException;
import com.ex.gateways.manage.restgateways.error.GatewayNotFoundException;
import com.ex.gateways.manage.restgateways.error.GatewaySameIpAddressException;
import com.ex.gateways.manage.restgateways.model.Gateway;
import com.ex.gateways.manage.restgateways.service.gateway.GatewayService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@RestController
@Description(value = "")
@RequestMapping(path = "/api/gateway")
@Api(value = "Gateway Management", description = "Store information about these gateways and their associated devices, as well as obtain information about the devices associated with the gateways")
public class ApiGatewayController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GatewayService gatewayService;

    @ApiOperation(value = "Add a gateway and their peripheral")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Gateway created successfully "),
            @ApiResponse(code = 500, message = "An error occur creating gateway"),
    })
    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<MessageEventDto> addGateway(@ApiParam(value = "Json with gateway data", required = true) @RequestBody GatewayDto gatewayDto) {
        try {
            Gateway gateway = convertToGatewayEntity(gatewayDto);
            MessageEventDto messageEventDto = gatewayService.addGateway(gateway);
            return new ResponseEntity<>(messageEventDto, HttpStatus.CREATED);
        } catch (GatewaySameIpAddressException e) {
            return new ResponseEntity<>(new MessageEventDto(e.getMessage(), MessageEventType.ERROR, new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (GatewayException e) {
            return new ResponseEntity<>(new MessageEventDto(e.getMessage(), MessageEventType.ERROR, new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get all gateways stored")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all gateways stored"),
            @ApiResponse(code = 500, message = "Error getting all gateways stored")
    })
    @GetMapping(path = "/all", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<GatewayDto>> getAllGateways() {
        try {
            List<GatewayDto> allGateways = gatewayService.getAllGateways();
            return new ResponseEntity<>(allGateways, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<GatewayDto>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get details of a gateway")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gateway found"),
            @ApiResponse(code = 404, message = "Gateway not found"),
            @ApiResponse(code = 500, message = "Error getting gateways detail")
    })
    @GetMapping(path = "/detail/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<GatewayDto> getGatewayDetail(@ApiParam(value = "Id of gateway store", required = true) @PathVariable String id) {
        try {
            long idGateway = Long.parseLong(id);
            GatewayDto gatewayDetails = gatewayService.getGatewayDetails(idGateway);
            return new ResponseEntity<>(gatewayDetails, HttpStatus.OK);
        } catch (GatewayNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (GatewayException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Gateway convertToGatewayEntity(GatewayDto gatewayDto) {
        return modelMapper.map(gatewayDto, Gateway.class);
    }
}
