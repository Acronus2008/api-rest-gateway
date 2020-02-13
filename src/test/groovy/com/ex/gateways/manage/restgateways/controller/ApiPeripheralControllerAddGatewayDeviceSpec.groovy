package com.ex.gateways.manage.restgateways.controller

import com.ex.gateways.manage.restgateways.dto.GatewayDto
import com.ex.gateways.manage.restgateways.utils.GatewayStatus
import com.ex.gateways.manage.restgateways.dto.PeripheralDeviceDto
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
class ApiPeripheralControllerAddGatewayDeviceSpec extends Specification {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private PeripheralDeviceRepository peripheralDeviceRepository

    @Autowired
    private ApiPeripheralController apiPeripheralController

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

    def "when add a device for a gateway and has more than 10 elements then the status code is 500 indicating that the device gateway not created"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        and: "creating a peripheral devices for the gateway"
        PeripheralDeviceDto peripheralDevice = new PeripheralDeviceDto(12345, "aPeripheralVendor", new Date(), GatewayStatus.ONLINE)

        and: "a gateway"
        List<Gateway> gatewayList = gatewayRepository.findAll().findAll { it -> it.name.contains("test_") }
        Gateway gatewayToUpdate = gatewayList.first()

        when:
        ResponseEntity<GatewayDto> gateway = apiPeripheralController.addDeviceToGateway(peripheralDevice, gatewayToUpdate.getId().toString())

        then:
        gateway.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR

        and: "the device was not created"
        gatewayToUpdate.getPeripheralDevices().find { it -> it.getUid() == 12345 }.getId() == null
    }

    def "when add a device for a gateway then return a status code 200 and return the gateway with the gateway updated with the device added"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        and: "creating a peripheral devices for the gateway"
        PeripheralDeviceDto peripheralDevice = new PeripheralDeviceDto(12345, "aPeripheralVendor", new Date(), GatewayStatus.ONLINE)

        and: "a gateway"
        storeGateway(115, 1)
        List<Gateway> gatewayList = gatewayRepository.findAll().findAll { it -> it.name.contains("test_115") }
        Gateway gatewayToUpdate = gatewayList.first()

        when:
        ResponseEntity<GatewayDto> gateway = apiPeripheralController.addDeviceToGateway(peripheralDevice, gatewayToUpdate.getId().toString())

        then:
        gateway.getStatusCode() == HttpStatus.OK

        and: "found the device added"
        GatewayDto gatewayDto = gateway.getBody()
        PeripheralDeviceDto gatewayDeviceAdded = gatewayDto.getPeripheralDevices().find { it -> (it.getUid() == 12345) }
        gatewayDeviceAdded != null

        and: "the list of device is granted than the old list because was added a new device"
        Optional<PeripheralDevice> peripheral = findPeripheralDeviceByUid(peripheralDevice.getUid())
        peripheral.get().getUid() == peripheralDevice.getUid()
    }

    def "when try to add a device for a gateway and the gateway not found then return a status code 404"() {
        given:
        MockHttpServletRequest request = new MockHttpServletRequest()
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request))

        and: "creating a peripheral devices for the gateway"
        PeripheralDeviceDto peripheralDevice = new PeripheralDeviceDto(123456, "aPeripheralVendor", new Date(), GatewayStatus.ONLINE)

        and: "create new gateway"
        storeGateway(120, 1)

        when:
        ResponseEntity<GatewayDto> gateway = apiPeripheralController.addDeviceToGateway(peripheralDevice, "12345")

        then:
        gateway.getStatusCode() == HttpStatus.NOT_FOUND

        and: "the list of device was not updated"
        Optional<PeripheralDevice> peripheral = findPeripheralDeviceByUid(peripheralDevice.getUid())
        try {
            peripheral.get()
        } catch (NoSuchElementException e) {
            e.getMessage() == "No value present"
        }
    }

    private Optional<PeripheralDevice> findPeripheralDeviceByUid(long uid) {
        Optional<PeripheralDevice> peripheral = peripheralDeviceRepository.findPeripheralDeviceByUid(uid)
        peripheral
    }

    private void createFakeGateways() {
        (0..3).each { i ->
            storeGateway(i, 9)
        }
    }

    private void storeGateway(int i, int devices) {
        Gateway gateway = new Gateway()
        gateway.setName(String.format("test_%s", i))
        gateway.setSerialNumber(i.toString())
        gateway.setIpAddress(String.format("1.1.1.%s", i))

        def peripheralDevices = getListOfPeripheralDevices(gateway, devices)
        gateway.setPeripheralDevices(new HashSet<PeripheralDevice>(peripheralDevices))

        gatewayRepository.save(gateway)
    }

    private void deleteFakesGateways() {
        List<PeripheralDevice> peripheralDevices = peripheralDeviceRepository.findAll().findAll { it -> it.vendor.contains("test") }

        peripheralDevices.each { it ->
            peripheralDeviceRepository.deleteById(it.id)
        }
    }

    private static Set<PeripheralDevice> getListOfPeripheralDevices(Gateway gateway, int devices) {
        Set<PeripheralDevice> peripheralDevices = new HashSet<>()

        (0..devices).each { it ->
            peripheralDevices.add(new PeripheralDevice(null, it, String.format("test %s", it.toString()), new Date(), GatewayStatus.ONLINE.toString(), gateway))
        }

        peripheralDevices
    }
}
