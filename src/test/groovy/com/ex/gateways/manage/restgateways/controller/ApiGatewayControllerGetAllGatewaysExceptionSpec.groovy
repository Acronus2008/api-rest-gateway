package com.ex.gateways.manage.restgateways.controller

import com.ex.gateways.manage.restgateways.ApiTestContextConfigurator
import com.ex.gateways.manage.restgateways.dto.GatewayDto
import com.ex.gateways.manage.restgateways.repository.GatewayRepository
import com.ex.gateways.manage.restgateways.repository.PeripheralDeviceRepository
import com.ex.gateways.manage.restgateways.service.gateway.GatewayService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import spock.lang.Specification

@AutoConfigureMockMvc
@WebMvcTest
@Import([ApiTestContextConfigurator.StubConfig])
class ApiGatewayControllerGetAllGatewaysExceptionSpec extends Specification {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private PeripheralDeviceRepository peripheralDeviceRepository

    @Autowired
    private ApiGatewayController apiGatewayController

    @Autowired
    private GatewayRepository gatewayRepository


    def "when try to get all the gateways stored and exception occur then return and empty list of gateways and the status code is 500"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        and:
        gatewayService.getAllGateways() >> { throw new Exception() }

        when:
        ResponseEntity<List<GatewayDto>> gateways = apiGatewayController.getAllGateways()

        then:
        gateways.getBody().findAll { it -> it.name.contains("test_") }.size() == 0

        and:
        gateways.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
    }
}
