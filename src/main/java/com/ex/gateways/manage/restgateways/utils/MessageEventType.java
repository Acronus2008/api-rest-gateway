package com.ex.gateways.manage.restgateways.utils;

public enum MessageEventType {
    ERROR("ERROR"), INFO("INFO"), WARN("WARNING");

    public final String value;

    private MessageEventType(String value) {
        this.value = value;
    }
}
