package com.ex.gateways.manage.restgateways.utils;

public enum GatewayStatus {
    ONLINE("online"), OFFLINE("offline");

    public final String value;

    private GatewayStatus(String value) {
        this.value = value;
    }
}
