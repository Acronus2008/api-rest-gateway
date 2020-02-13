package com.ex.gateways.manage.restgateways

import org.awaitility.Awaitility
import org.hamcrest.Matcher
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.util.concurrent.TimeUnit

@SpringBootTest(classes = RestGatewaysApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles( ["test"] )
class IntegrationSpecification extends Specification {
    void awaitFor(Closure closure) {
        Awaitility.with().pollInterval(1000, TimeUnit.MILLISECONDS).await().until({
            call: {
                closure()
            }
        })
    }

    void awaitFor(Closure closure, Matcher matcher) {
        Awaitility.with().pollInterval(1000, TimeUnit.MILLISECONDS).await().until({
            call: {
                closure()
            }
        }, matcher)
    }

}
