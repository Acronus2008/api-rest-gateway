package com.ex.gateways.manage.restgateways

import com.ex.gateways.manage.restgateways.controller.ApiGatewayController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class LoadContextSpec extends Specification {
    @Autowired(required = false)
    private ApiGatewayController apiGatewayController

    def "when context is loaded then all expected beans are created"() {
        expect: "the WebController is created"
        apiGatewayController
    }

}
