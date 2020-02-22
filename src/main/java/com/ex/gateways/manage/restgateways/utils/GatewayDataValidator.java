package com.ex.gateways.manage.restgateways.utils;

import com.ex.gateways.manage.restgateways.error.GatewayException;
import com.ex.gateways.manage.restgateways.model.Gateway;
import com.google.common.net.InetAddresses;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GatewayDataValidator {

    public void validateGatewayData(Gateway gateway) throws GatewayException {
        validator(gateway.getPeripheralDevices().size() > 10, "Only up to 10 devices are allowed per gateway");

        validateGatewayIpAddress(gateway.getIpAddress());

        validateGatewayName(gateway.getName());
    }

    private void validateGatewayName(String gatewayName) throws GatewayException {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\-\\.]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$");
        Matcher gatewayMatch = pattern.matcher(gatewayName);

        if (!gatewayMatch.matches()) {
            throw new GatewayException("The Gateway name not a human-readable name");
        }
    }

    private void validateGatewayIpAddress(String ipAddress) throws GatewayException {
        boolean isValidIpAddress = isValidIpAddress(ipAddress);

        validator(!isValidIpAddress, String.format("The IP Address for this gateway is invalid: %s", ipAddress));
    }

    private boolean isValidIpAddress(String ip) {

        if (ip == null || ip.trim().equals("")) {
            return false;
        }

        return InetAddresses.isInetAddress(ip);
    }

    private void validator(boolean b, String s) throws GatewayException {
        if (b) {
            throw new GatewayException(s);
        }
    }
}
