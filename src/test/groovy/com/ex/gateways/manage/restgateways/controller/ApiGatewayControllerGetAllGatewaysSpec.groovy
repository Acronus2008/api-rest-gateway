package com.ex.gateways.manage.restgateways.controller


import com.ex.gateways.manage.restgateways.dto.GatewayDto
import com.ex.gateways.manage.restgateways.utils.GatewayStatus
import com.ex.gateways.manage.restgateways.model.Gateway
import com.ex.gateways.manage.restgateways.model.PeripheralDevice
import com.ex.gateways.manage.restgateways.repository.GatewayRepository
import com.ex.gateways.manage.restgateways.repository.PeripheralDeviceRepository
import com.ex.gateways.manage.restgateways.service.gateway.GatewayService
import org.junit.runner.RunWith
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import spock.lang.Specification

import javax.transaction.Transactional

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class ApiGatewayControllerGetAllGatewaysSpec extends Specification {

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

    private List<GatewayDto> gatewayDtoList

    def setup() {
        gatewayDtoList = new ArrayList<>()

        createFakeGateways()
    }

    def cleanup() {
        deleteFakesGateways()
    }

    def "when try to get all the gateways stored the return a JSON with list of gateways and the peripheral devices associate and the status code is 200"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        when:
        ResponseEntity<List<GatewayDto>> gateways = apiGatewayController.getAllGateways()

        then:
        gateways.getBody().findAll { it -> it.name.contains("test_") }.size() == 4

        and:
        gateways.getStatusCode() == HttpStatus.OK
    }


    private void createFakeGateways() {
        (0..3).each { i ->
            Gateway gateway = new Gateway()
            gateway.setName(String.format("test_%s", i))
            gateway.setSerialNumber(i.toString())
            gateway.setIpAddress(String.format("1.1.1.%s", i))

            def peripheralDevices = getListOfPeripheralDevices(gateway)
            gateway.setPeripheralDevices(new HashSet<PeripheralDevice>(peripheralDevices))

            gatewayRepository.save(gateway)
        }
    }

    private void deleteFakesGateways() {
        List<PeripheralDevice> peripheralDevices = peripheralDeviceRepository.findAll().findAll { it -> it.vendor.contains("test") }

        peripheralDevices.each { it ->
            peripheralDeviceRepository.deleteById(it.id)
        }
    }

    private Set<PeripheralDevice> getListOfPeripheralDevices(Gateway gateway) {
        Set<PeripheralDevice> peripheralDevices = new ArrayList<>()

        (0..9).each { it ->
            peripheralDevices.add(new PeripheralDevice(null, it, String.format("test %s", it.toString()), new Date(), GatewayStatus.ONLINE.toString(), gateway))
        }

        peripheralDevices
    }
}
