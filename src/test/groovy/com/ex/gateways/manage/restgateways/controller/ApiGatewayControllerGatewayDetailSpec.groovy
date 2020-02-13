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
class ApiGatewayControllerGatewayDetailSpec extends Specification {

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

    def "when try to get a detail of a gateways stored and the gateway don't exist then return 404 http error "() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        when:
        ResponseEntity<GatewayDto> gateway = apiGatewayController.getGatewayDetail("100")

        then:
        gateway.getStatusCode() == HttpStatus.NOT_FOUND
    }

    def "when try to get a detail of a gateway and exist the return the gateway with the devices associated"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        and:
        Gateway gateway = gatewayRepository.findAll().findAll {it->it.name.contains("test_")}.first()

        when:
        ResponseEntity<GatewayDto> gatewayDetail = apiGatewayController.getGatewayDetail(gateway.id.toString())

        then:
        gatewayDetail.getBody().getPeripheralDevices().size() == 10

        and:
        gatewayDetail.getStatusCode() == HttpStatus.OK
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

    private static Set<PeripheralDevice> getListOfPeripheralDevices(Gateway gateway) {
        Set<PeripheralDevice> peripheralDevices = new HashSet<>()

        (0..9).each { it ->
            peripheralDevices.add(new PeripheralDevice(null, it, String.format("test %s", it.toString()), new Date(), GatewayStatus.ONLINE.toString(), gateway))
        }

        peripheralDevices
    }
}
