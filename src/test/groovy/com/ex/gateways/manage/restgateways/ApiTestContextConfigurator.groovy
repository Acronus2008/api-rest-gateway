package com.ex.gateways.manage.restgateways

import com.ex.gateways.manage.restgateways.repository.GatewayRepository
import com.ex.gateways.manage.restgateways.repository.PeripheralDeviceRepository
import com.ex.gateways.manage.restgateways.service.gateway.GatewayService
import com.ex.gateways.manage.restgateways.service.gateway.IGatewayService
import com.ex.gateways.manage.restgateways.service.peripheral.IPeripheralDeviceService
import com.ex.gateways.manage.restgateways.service.peripheral.PeripheralDeviceService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import spock.mock.DetachedMockFactory

class ApiTestContextConfigurator {
    @TestConfiguration
    static class StubConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()


        @Bean
        IGatewayService iGatewayService() {
            return detachedMockFactory.Stub(IGatewayService)
        }

        @Bean
        GatewayService gatewayService() {
            return detachedMockFactory.Stub(GatewayService)
        }

        @Bean
        IPeripheralDeviceService iPeripheralDeviceService() {
            return detachedMockFactory.Stub(IPeripheralDeviceService)
        }

        @Bean
        PeripheralDeviceService peripheralDeviceService() {
            return detachedMockFactory.Stub(PeripheralDeviceService)
        }

        @Bean
        GatewayRepository gatewayRepository() {
            return detachedMockFactory.Stub(GatewayRepository)
        }

        @Bean
        PeripheralDeviceRepository peripheralDeviceRepository() {
            return detachedMockFactory.Stub(PeripheralDeviceRepository)
        }
    }
}
