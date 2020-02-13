package com.ex.gateways.manage.restgateways.error;

public class GatewaySameIpAddressException extends Exception {
    public GatewaySameIpAddressException(String message) {
        super(message);
    }
}
