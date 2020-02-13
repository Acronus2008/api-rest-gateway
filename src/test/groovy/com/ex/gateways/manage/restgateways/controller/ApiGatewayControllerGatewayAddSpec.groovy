package com.ex.gateways.manage.restgateways.controller

import com.ex.gateways.manage.restgateways.dto.GatewayDto
import com.ex.gateways.manage.restgateways.utils.GatewayStatus
import com.ex.gateways.manage.restgateways.dto.PeripheralDeviceDto
import com.ex.gateways.manage.restgateways.dto.event.MessageEventDto
import com.ex.gateways.manage.restgateways.utils.MessageEventType
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
class ApiGatewayControllerGatewayAddSpec extends Specification {

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

    def "when add a gateways then is stored in database, the status is 201 and return a message indicating that the gateway was created"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        and: "creating a peripheral devices for the gateway"
        List<PeripheralDeviceDto> peripheralDevices = new ArrayList<>()
        peripheralDevices.add(new PeripheralDeviceDto(1, "aPeripheralVendor", new Date(), GatewayStatus.ONLINE))

        and: "creating the gateway"
        GatewayDto gatewayDto = new GatewayDto("aSerialNumber_1", "aGatewayName", "1.1.1.1", peripheralDevices)

        when:
        ResponseEntity<MessageEventDto> messageEvent = apiGatewayController.addGateway(gatewayDto)

        then:
        messageEvent.getStatusCode() == HttpStatus.CREATED

        and:
        MessageEventDto event = messageEvent.getBody()
        event.getType() == MessageEventType.INFO

        and:
        event.getMessage() == String.format("Gateway created name:%s ip:%s serial number:%s devices:%s", gatewayDto.getName(), gatewayDto.getIpAddress(), gatewayDto.getSerialNumber(), gatewayDto.getPeripheralDevices().size())
    }

    def "when try to add a gateway with and invalid ip address then the status code is 500 and return a message indicating the error"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        and: "creating a peripheral devices for the gateway"
        List<PeripheralDeviceDto> peripheralDevices = new ArrayList<>()
        peripheralDevices.add(new PeripheralDeviceDto(1, "aPeripheralVendor", new Date(), GatewayStatus.ONLINE))

        and: "creating the gateway"
        GatewayDto gatewayDto = new GatewayDto("aSerialNumber_1", "aGatewayName", "1.1.1.", peripheralDevices)

        when:
        ResponseEntity<MessageEventDto> messageEvent = apiGatewayController.addGateway(gatewayDto)

        then:
        messageEvent.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR

        and:
        MessageEventDto event = messageEvent.getBody()
        event.getType() == MessageEventType.ERROR

        and:
        event.getMessage() == String.format("An error occurred adding a gateway: The IP Address for this gateway is invalid: %s", gatewayDto.getIpAddress())
    }

    def "when try to add a gateway with and empty ip address then the status code is 500 and return a message indicating the error"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        and: "creating a peripheral devices for the gateway"
        List<PeripheralDeviceDto> peripheralDevices = new ArrayList<>()
        peripheralDevices.add(new PeripheralDeviceDto(1, "aPeripheralVendor", new Date(), GatewayStatus.ONLINE))

        and: "creating the gateway"
        GatewayDto gatewayDto = new GatewayDto("aSerialNumber_1", "aGatewayName", "1.1.1.", peripheralDevices)

        when:
        ResponseEntity<MessageEventDto> messageEvent = apiGatewayController.addGateway(gatewayDto)

        then:
        messageEvent.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR

        and:
        MessageEventDto event = messageEvent.getBody()
        event.getType() == MessageEventType.ERROR

        and:
        event.getMessage() == String.format("An error occurred adding a gateway: The IP Address for this gateway is invalid: %s", gatewayDto.getIpAddress())
    }

    def "when try to add a gateway with an existing ip address then the status code is 500 and return a message indicating the error"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        and: "creating a peripheral devices for the gateway"
        List<PeripheralDeviceDto> peripheralDevices = new ArrayList<>()
        peripheralDevices.add(new PeripheralDeviceDto(1, "aPeripheralVendor", new Date(), GatewayStatus.ONLINE))

        and: "creating the gateway"
        GatewayDto gatewayDto = new GatewayDto("aSerialNumber_1", "aGatewayName", "1.1.1.3", peripheralDevices)

        when:
        ResponseEntity<MessageEventDto> messageEvent = apiGatewayController.addGateway(gatewayDto)

        then:
        messageEvent.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR

        and:
        MessageEventDto event = messageEvent.getBody()
        event.getType() == MessageEventType.ERROR

        and:
        event.getMessage() == String.format("An error occurred adding a gateway: A gateway with the same ip address:%s exist", gatewayDto.getIpAddress())
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
